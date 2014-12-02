package ac.uk.susx.tag.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.writer.StandardOutputWriter;

public class ConcurrentLineProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 10;
	private final IConfiguration config;
	private final ArrayBlockingQueue<Future<Boolean>> queue;
	private boolean complete;
	
	public ConcurrentLineProcessor(IConfiguration config) {
		this.config = config;
		queue = new ArrayBlockingQueue<Future<Boolean>>(NTHREADS*2);
	}

	public void processFiles(String filesDir) {
		ExecutorService producerPool = Executors.newSingleThreadExecutor();
		ExecutorService consumerPool = Executors.newSingleThreadExecutor();
		producerPool.submit(new Producer(queue,filesDir));
		consumerPool.submit(new Consumer(queue));
		producerPool.shutdown();
		try {
			producerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			queue.add(new ConsumerShutdownFuture());
			consumerPool.shutdown();
			consumerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	public void processFile(File file) {
		processFiles(file.getParentFile().getAbsolutePath());
	}
	
	private final class Producer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		private final String filesDir;

		private Producer(ArrayBlockingQueue<Future<Boolean>> queue, String filesDir){
			this.queue = queue;
			this.filesDir = filesDir;
		}
		
		public void run() {
			ExecutorService executor;
			Iterator<File> iter = FileUtils.iterateFiles(new File(filesDir), new String[] {config.getInputSuff()}, true);
			while(iter.hasNext()){
				try {
					File next = iter.next();
					executor = Executors.newFixedThreadPool(NTHREADS);
					BufferedReader br = new BufferedReader(new FileReader(next));
					String currLine = br.readLine();
					StandardOutputWriter writer = new StandardOutputWriter(config.getOutputLocation() + "/" + next.getName());
					int lineCount = 0;
					while(currLine != null){
						String line = new String(currLine).trim();
						if(line.length() > 0) {
							DocumentCallable docCaller = new DocumentCallable(config.getDocumentBuilder().createDocument(line),writer);
							queue.put(executor.submit(docCaller));
						}
						currLine = br.readLine();
						lineCount++;
						if(lineCount%1000 == 0){
							System.err.println("Processing line: " + lineCount + " of File: " + next.getName());
						}
					}
					executor.shutdown();
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
					System.err.println("Finished processing file: " + next.getName());
					writer.closeDocument();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	private final class Consumer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		
		private Consumer(ArrayBlockingQueue<Future<Boolean>> queue){
			this.queue = queue;
		}

		public void run() {
			try {
				while(!complete){
					Future<Boolean> out = queue.take();
					if(out.get() == false){
						complete = true;
						return;
					}
					complete = out instanceof ConsumerShutdownFuture;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return;
			}
		}
		
	}
	
	private final class DocumentCallable implements Callable<Boolean> {
			
			private final Document document;
			private final StandardOutputWriter writer;
			
			public DocumentCallable(Document document, StandardOutputWriter writer){
				this.document = document;
				this.writer = writer;
			}
	
			public Boolean call() throws Exception {
				for(IAnnotator<?,?> annotator : config.getAnnotators()){
					try {
						annotator.annotate(document);
					} catch (IncompatibleAnnotationException e) {
						e.printStackTrace();
						return false;
					}
				}
				document.retainDocumentAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
				document.filterDocumentAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
				config.getOutputWriter().processSubDocument(writer, document);
				return true;
			}
	}
	
	// Used as a poison pill to shut down the consumer object.
	private final class ConsumerShutdownFuture implements Future<Boolean> {

		public boolean cancel(boolean arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		public Boolean get() throws InterruptedException, ExecutionException {
			// TODO Auto-generated method stub
			return null;
		}

		public Boolean get(long arg0, TimeUnit arg1) throws InterruptedException,
				ExecutionException, TimeoutException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isCancelled() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}

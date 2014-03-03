package ac.uk.susx.tag.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.writer.CharSequenceWriter;

public class ConcurrentLineProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 10;
	private final IConfiguration<CharSequence> config;
	private final ArrayBlockingQueue<Future<Boolean>> queue;
	private boolean complete;
	
	public ConcurrentLineProcessor(IConfiguration<CharSequence> config) {
		this.config = config;
		queue = new ArrayBlockingQueue<Future<Boolean>>(NTHREADS*2);
	}

	public void processFiles(List<File> files) {
		ExecutorService producerPool = Executors.newSingleThreadExecutor();
		ExecutorService consumerPool = Executors.newSingleThreadExecutor();
		producerPool.submit(new Producer(queue,files));
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
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
	private final class Producer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		private final List<File> files;

		private Producer(ArrayBlockingQueue<Future<Boolean>> queue, List<File> files){
			this.queue = queue;
			this.files = files;
		}
		
		public void run() {
			ExecutorService executor;
			for(File file : files){
				try {
					executor = Executors.newFixedThreadPool(NTHREADS);
					BufferedReader br = new BufferedReader(new FileReader(file));
					String currLine = br.readLine();
					CharSequenceWriter writer = new CharSequenceWriter(config.getOutputLocation() + "/" + file.getName());
					int lineCount = 0;
					while(currLine != null){
						String line = new String(currLine);
						DocumentCallable docCaller = new DocumentCallable(config.getDocumentBuilder().createDocument(line),writer);
						queue.put(executor.submit(docCaller));
						currLine = br.readLine();
						lineCount++;
						if(lineCount%1000 == 0){
							System.err.println("Processing line: " + lineCount + " of File: " + file.getName());
						}
					}
					
					executor.shutdown();
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
					System.err.println("Finished processing file: " + file.getName());
					writer.closeDocument();
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public final class Consumer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		
		private Consumer(ArrayBlockingQueue<Future<Boolean>> queue){
			this.queue = queue;
		}

		public void run() {
			try {
				while(!complete){
					Future<Boolean> out = queue.take();
					out.get();
					complete = out instanceof ConsumerShutdownFuture;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public final class DocumentCallable implements Callable<Boolean> {
			
			private final IDocument document;
			private final CharSequenceWriter writer;
			
			public DocumentCallable(IDocument document, CharSequenceWriter writer){
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
	public final class ConsumerShutdownFuture implements Future<Boolean> {

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

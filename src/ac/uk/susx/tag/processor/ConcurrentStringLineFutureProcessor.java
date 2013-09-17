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

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.writer.StringWriter;

public class ConcurrentStringLineFutureProcessor implements Processor<String,String> {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final Configuration<Document<String,String>,String,String> config;
	private final ArrayBlockingQueue<Future<Boolean>> queue;
	private boolean complete;
	
	public ConcurrentStringLineFutureProcessor(Configuration<Document<String,String>,String,String> config) {
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
			complete = producerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			System.err.println("terminated prod pool");
			System.err.println(complete);
			consumerPool.shutdown();
			consumerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			System.err.println("terminated cons pool");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private final class Producer implements Runnable {
		
		private final ArrayBlockingQueue<Future<Boolean>> queue;
		private final List<File> files;
		private final ExecutorService executor;

		private Producer(ArrayBlockingQueue<Future<Boolean>> queue, List<File> files){
			this.queue = queue;
			this.files = files;
			executor = Executors.newFixedThreadPool(NTHREADS);
		}
		
		public void run() {
			for(File file : files){
				try {
					BufferedReader br = new BufferedReader(new FileReader(file));
					String currLine = br.readLine();
					StringWriter writer = new StringWriter(config.getOutputLocation() + "/" + file.getName());
					int lineCount = 0;
					while(currLine != null){
						String line = currLine;
						StringDocument document = new StringDocument(line);
						DocumentCallable docCaller = new DocumentCallable(document,writer);
						queue.put(executor.submit(docCaller));
						currLine = br.readLine();
						lineCount++;
						if(lineCount%1000 == 0){
							System.err.println("Processing line: " + lineCount + " of File: " + file.getName());
						}
					}
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
					queue.take().get();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void processFile(File file) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
	public final class DocumentCallable implements Callable<Boolean> {
			
			private final Document<String,String> document;
			private final StringWriter writer;
			
			public DocumentCallable(Document<String, String> document, StringWriter writer){
				this.document = document;
				this.writer = writer;
			}
	
			public Boolean call() throws Exception {
				for(Annotator<Document<String,String>,?,String,String> annotator : config.getAnnotators()){
					try {
						annotator.annotate(document);
					} catch (IncompatibleAnnotationException e) {
						e.printStackTrace();
						return false;
					}
				}
				document.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
				document.filterAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
				config.getOutputWriter().processSubDocument(writer, document.sortAnnotations(config.getOutputIncludedAnnotators()));
				return true;
			}
	}
	
	public final class DocumentShutdown implements Callable<Boolean> {
		
		private final StringWriter writer;
		
		public DocumentShutdown(StringWriter writer, String fileName){
			System.err.println("Finished processing file: " + fileName);
			this.writer = writer;
		}
	
		public Boolean call() throws Exception {
			writer.closeDocument();
			return true;
		}
		
	}

}

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

import org.apache.commons.io.FileUtils;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.writer.OutputWriter;

public class ConcurrentLineProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 10;
	private final IConfiguration config;
	private final ArrayBlockingQueue<Future<Document>> queue;
	private boolean complete;
	
	public ConcurrentLineProcessor(IConfiguration config) {
		this.config = config;
		queue = new ArrayBlockingQueue<Future<Document>>(NTHREADS*2);
	}

	public void processFiles(String filesDir) {
		ExecutorService producerPool = Executors.newSingleThreadExecutor();
		ExecutorService consumerPool = Executors.newSingleThreadExecutor();
		producerPool.submit(new Producer(filesDir));
		consumerPool.submit(new Consumer(queue));
		producerPool.shutdown();
		try {
			producerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			complete = true;
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
		
		private final String filesDir;
		private final ExecutorService executor;

		private Producer(String filesDir){
			this.filesDir = filesDir;
			executor = Executors.newFixedThreadPool(NTHREADS);
		}
		
		public void run() {

			Iterator<File> iter = FileUtils.iterateFiles(new File(filesDir), new String[] {config.getInputSuff()}, true);
			while(iter.hasNext()){
				try {
					File next = iter.next();
					BufferedReader br = new BufferedReader(new FileReader(next));
					String currLine = br.readLine();
					int lineCount = 0;
					while(currLine != null){
						String line = new String(currLine).trim();
						if(line.length() > 0) {
							DocumentCallable docCaller = new DocumentCallable(config.getDocumentBuilder().createDocument(line));
							queue.put(executor.submit(docCaller));
						}
						currLine = br.readLine();
						lineCount++;
						if(lineCount % 1000 == 0){
							System.err.println("Processing line: " + lineCount + " of File: " + next.getName());
						}
					}
					executor.shutdown();
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
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
		
		private final ArrayBlockingQueue<Future<Document>> queue;
		
		private Consumer(ArrayBlockingQueue<Future<Document>> queue){
			this.queue = queue;
		}

		public void run() {
			try {
				while(complete == false || !queue.isEmpty()){
					if(queue.isEmpty()) {
						continue;
					}
					Future<Document> out = queue.take();
					Document doc = out.get();
					OutputWriter writer = null;
					try {
						writer = new OutputWriter(config.getOutputLocation() + "/" + doc.getName());
					} catch (IOException e) {
						e.printStackTrace();
					}
					config.getOutputFormatter().processDocument(writer, doc);
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
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
	
	private final class DocumentCallable implements Callable<Document> {
			
			private final Document document;
			
			public DocumentCallable(Document document){
				this.document = document;
			}
	
			public Document call() throws Exception {
				for(IAnnotator<?,?> annotator : config.getAnnotators()){
					try {
						annotator.annotate(document);
					} catch (IncompatibleAnnotationException e) {
						e.printStackTrace();
					}
				}
				document.retainDocumentAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
				document.filterDocumentAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
				return document;
			}
	}

}

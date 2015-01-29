package ac.uk.susx.tag.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
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
	private final String singDocName;
	private final IConfiguration config;
	private final ArrayBlockingQueue<Future<Document>> queue;
	private boolean complete;
	private boolean singleFile;
	
	public ConcurrentLineProcessor(IConfiguration config, boolean singleFile) {
		this.singDocName = "output-file-" + ((new Date().getTime())/1000);
		this.config = config;
		this.singleFile = singleFile;
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
	

	/**
	 * TODO: Re-write as is broken.
	 */
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
						final String line = new String(currLine).trim();
						if(line != null && line.length() > 0 && !line.isEmpty()) {
							final DocumentCallable docCaller;
							if(!singleFile) {
								docCaller = new DocumentCallable(config.getDocumentBuilder().createDocument(line, next.getName()));
							}
							else{ // Writes all output to one file.
								docCaller = new DocumentCallable(config.getDocumentBuilder().createDocument(line, singDocName));
							}
							try {
								queue.put(executor.submit(docCaller));
							} catch(Exception e) {
								e.printStackTrace();
							}
						}
						currLine = br.readLine();
						lineCount++;
						if(lineCount % 1000 == 0) {
							System.err.println("Processing line: " + lineCount + " of File: " + next.getName());
						}
					}
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			executor.shutdown();
			try {
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
					try {
						config.getOutputFormatter().processDocument(new OutputWriter(config.getOutputLocation() + "/" + doc.getName()), doc);
					} catch (IOException e1) {
						e1.printStackTrace();
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
				try {
					document.retainDocumentAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
					document.filterDocumentAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
				} catch (Exception e) {
					e.printStackTrace();
				}
				return document;
			}
	}

}

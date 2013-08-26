package ac.uk.susx.tag.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.writer.StringWriter;

public class ConcurrentStringLineProcessor implements Processor<String,String>{
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final Configuration<Document<String,String>,String,String> config;
	
	public ConcurrentStringLineProcessor(Configuration<Document<String,String>,String,String> config){
		this.config = config;
	}

	public void processFiles(List<File> files) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for(File file : files){
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				StringWriter writer = new StringWriter(config.getOutputLocation() + "/" + file.getName());
				String currLine = br.readLine();
				while(currLine != null){
					int iter = 0;
					ArrayList<Callable<Boolean>> batch = new ArrayList<Callable<Boolean>>();
					while(iter < 20 || currLine != null) {
						String line = currLine;
						StringDocument document = new StringDocument(line);
						DocumentCallable docCaller = new DocumentCallable(document,writer);
						batch.add(docCaller);
						iter++;
						currLine = br.readLine();
					}
					try {
						executor.invokeAll(batch);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				ArrayList<Callable<Boolean>> shutdown = new ArrayList<Callable<Boolean>>();
				shutdown.add(new DocumentShutdown(writer, file.getName()));
				br.close();
				try {
					executor.invokeAll(shutdown);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		executor.shutdown();
	}

	public void processFile(File file) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
public class DocumentCallable implements Callable<Boolean> {
		
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

public class DocumentShutdown implements Callable<Boolean> {
	
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

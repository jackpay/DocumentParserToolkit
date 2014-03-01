package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentDocumentProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final IConfiguration<CharSequence> config;
	
	public ConcurrentDocumentProcessor(IConfiguration<CharSequence> config){
		this.config = config;
	}
	
	public void processFiles(List<File> files){
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		final ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		for(File file : files){
			IDocument doc = config.getDocumentBuilder().createDocument(file.getAbsolutePath());
			Callable<Boolean> docCaller = new DocumentCallable(doc, file.getName());
			Future<Boolean> future = executor.submit(docCaller);
			futures.add(future); // Only really there to allow return values in the future.
		}
		executor.shutdown();
	}
	
	public void processFile(File file){
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
	public class DocumentCallable implements Callable<Boolean> {
		
		private final IDocument document;
		private final String fileName;
		
		public DocumentCallable(IDocument document, String fileName){
			this.document = document;
			this.fileName = fileName;
		}

		public Boolean call() throws Exception {
			System.err.println("Beginning processing of file: " + fileName);
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
			config.getOutputWriter().processDocument(config.getOutputLocation() + "/" + fileName, document);
			System.err.println("Processed file: " + fileName);
			return true;
		}
		
	}

}

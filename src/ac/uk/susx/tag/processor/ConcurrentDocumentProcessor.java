package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.configuration.GrammaticalConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentDocumentProcessor<AT,DT> {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final Configuration<Document<DT,AT>,AT,DT> config;
	OutputDocumentFormatter<AT> outputWriter;
	
	public ConcurrentDocumentProcessor(List<File> files, InputDocumentFormatter<AT,DT> docBuilder, Configuration<Document<DT,AT>,AT,DT> config) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		final ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		this.config = config;
		System.err.println(NTHREADS);
		for(File file : files){
			Document<DT,AT> doc = docBuilder.createDocument(file.getAbsolutePath());
			Callable<Boolean> docCaller = new DocumentCallable(doc,file.getName());
			Future<Boolean> future = executor.submit(docCaller);
			futures.add(future); // Only really there to allow return values in the future.
		}
		executor.shutdown();
	}
	
	public class DocumentCallable implements Callable<Boolean> {
		
		private final Document<DT,AT> document;
		private final String fileName;
		
		public DocumentCallable(Document<DT, AT> document, String fileName){
			this.document = document;
			this.fileName = fileName;
		}

		public Boolean call() throws Exception {
			for(Annotator<Document<DT,AT>,?,AT,DT> annotator : config.getAnnotators()){
				try {
					annotator.annotate(document);
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
					return false;
				}
			}
			document.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			config.getOutputWriter().processOutput(document, config.getOutputLocation() + "/" + fileName, GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator().getClass());
			System.err.println("Processed file: " + fileName);
			return true;
		}
		
	}

}

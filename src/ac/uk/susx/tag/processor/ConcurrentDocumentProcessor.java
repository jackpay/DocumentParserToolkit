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

public class ConcurrentDocumentProcessor<AT> {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final Configuration<Document<?,AT>,AT> config;
	OutputDocumentFormatter<AT> outputWriter;
	
	public ConcurrentDocumentProcessor(List<File> files, InputDocumentFormatter<AT> docBuilder, Configuration<Document<?,AT>,AT> config) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		final ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		this.config = config;
		for(File file : files){
			Document<?,AT> doc = docBuilder.createDocument(file.getAbsolutePath());
			Callable<Boolean> docCaller = new DocumentCallable(doc,file.getName());
			Future<Boolean> future = executor.submit(docCaller);
			futures.add(future); // Only really there to allow return values in the future.
		}
		
	}
	
	public class DocumentCallable implements Callable<Boolean> {
		
		private final Document<?,AT> document;
		private final String fileName;
		
		public DocumentCallable(Document<?, AT> document, String fileName){
			this.document = document;
			this.fileName = fileName;
		}

		public Boolean call() throws Exception {
			for(Annotator<Document<?,AT>,?,AT> annotator : config.getAnnotators()){
				try {
					annotator.annotate(document);
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
					return false;
				}
			}
			document.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			config.getOutputWriter().processOutput(document, config.getOutputLocation() + "/" + fileName, GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator().getClass());
			return true;
		}
		
	}

}

package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentStringSentenceProcessor implements Processor<String, String>{
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final Configuration<Document<String,String>,String,String> config;
	
	public ConcurrentStringSentenceProcessor(Configuration<Document<String,String>,String,String> config){
		this.config = config;
	}

	public void processFiles(List<File> files) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for(File file : files){
			Document<String, String> doc = config.getDocumentBuilder().createDocument(file.getAbsolutePath());
			final ArrayList<Future<Document<String,String>>> futures = new ArrayList<Future<Document<String,String>>>();
			try {
				StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(doc);
				Collection<? extends Annotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
				for(Annotation<String> sentence : sentences){
					SentenceCallable sentCaller = new SentenceCallable(sentence);
					Future<Document<String,String>> future = executor.submit(sentCaller);
					futures.add(future);
				}
				for(Future<Document<String,String>> future : futures){
					try {
						Document<String,String> sent = future.get();
						for(Class<? extends Annotator> annotator : sent.getDocumentAnnotations().keySet()){
							doc.addAnnotations(annotator, sent.getAnnotations(annotator));
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			} catch (IncompatibleAnnotationException e) {
				e.printStackTrace();
			}
			doc.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			config.getOutputWriter().processDocument(doc, config.getOutputLocation() + "/" + file.getName(), config.getHeadAnnotator());
		}
		executor.shutdown();
	}

	public void processFile(File file) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
public class SentenceCallable implements Callable<Document<String,String>> {
		
		private final Document<String,String> sentenceDoc;
		private final Annotation<String> sentenceAnn;
		
		//TODO: Add doc position to prevent clashes!
		public SentenceCallable(Annotation<String> sentence){
			this.sentenceAnn = sentence;
			sentenceDoc = new StringDocument(null);
			ArrayList<Annotation<String>> sentenceList = new ArrayList<Annotation<String>>();
			sentenceList.add(sentenceAnn);
			sentenceDoc.addAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass(), sentenceList);
		}

		public Document<String,String> call() throws Exception {
			for(Annotator<Document<String,String>,? extends Annotation<String>,String,String> annotator : config.getAnnotators()){
				try {
					ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
					annotations.addAll(annotator.annotate(sentenceDoc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass())));
					sentenceDoc.addAnnotations(annotator.getClass(), annotations);
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
				}
			}
			return sentenceDoc;
		}
		
	}
	
}

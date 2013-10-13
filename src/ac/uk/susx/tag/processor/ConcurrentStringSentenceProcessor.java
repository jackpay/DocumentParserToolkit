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

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.factory.StringAnnotatorEnum;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentStringSentenceProcessor implements IProcessor<String, String>{
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final IConfiguration<IDocument<String,String>,String,String> config;
	
	public ConcurrentStringSentenceProcessor(IConfiguration<IDocument<String,String>,String,String> config){
		this.config = config;
	}

	public void processFiles(List<File> files) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for(File file : files){
			IDocument<String, String> document = config.getDocumentBuilder().createDocument(file.getAbsolutePath());
			final ArrayList<Future<IDocument<String,String>>> futures = new ArrayList<Future<IDocument<String,String>>>();
			try {
				StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(document);
				Collection<? extends IAnnotation<String>> sentences = document.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
				for(IAnnotation<String> sentence : sentences){
					SentenceCallable sentCaller = new SentenceCallable(sentence);
					Future<IDocument<String,String>> future = executor.submit(sentCaller);
					futures.add(future);
				}
				for(Future<IDocument<String,String>> future : futures){
					try {
						IDocument<String,String> sent = future.get();
						for(Class<? extends IAnnotator> annotator : sent.getDocumentAnnotations().keySet()){
							document.addAnnotations(annotator, sent.getAnnotations(annotator));
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
			document.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			document.filterAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
			config.getOutputWriter().processDocument(config.getOutputLocation() + "/" + file.getName(), AnnotationUtils.collateAnnotations(document.getDocumentAnnotations(), config.getOutputIncludedAnnotators()));
		}
		executor.shutdown();
	}

	public void processFile(File file) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
public class SentenceCallable implements Callable<IDocument<String,String>> {
		
		private final IDocument<String,String> sentenceDoc;
		private final IAnnotation<String> sentenceAnn;
		
		//TODO: Add doc position to prevent clashes!
		public SentenceCallable(IAnnotation<String> sentence){
			this.sentenceAnn = sentence;
			sentenceDoc = new StringDocument(null);
			ArrayList<IAnnotation<String>> sentenceList = new ArrayList<IAnnotation<String>>();
			sentenceList.add(sentenceAnn);
			sentenceDoc.addAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass(), sentenceList);
		}

		public IDocument<String,String> call() throws Exception {
			for(IAnnotator<IDocument<String,String>,? extends IAnnotation<String>,String,String> annotator : config.getAnnotators()){
				try {
					ArrayList<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
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

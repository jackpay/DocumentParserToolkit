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
import ac.uk.susx.tag.annotator.registry.AnnotatorEnum;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentStringSentenceProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private final IConfiguration<CharSequence> config;
	
	public ConcurrentStringSentenceProcessor(IConfiguration<CharSequence> config){
		this.config = config;
	}

	public void processFiles(List<File> files) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for(File file : files){
			IDocument document = config.getDocumentBuilder().createDocument(file.getAbsolutePath());
			final ArrayList<Future<IDocument>> futures = new ArrayList<Future<IDocument>>();
			try {
				AnnotatorEnum.SENTENCE.getAnnotator().annotate(document);
				Collection<? extends IAnnotation<String>> sentences = document.getAnnotations(AnnotatorEnum.SENTENCE.getAnnotator().getClass());
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
			document.retainDocumentAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			document.filterDocumentAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
			config.getOutputWriter().processDocument(config.getOutputLocation() + "/" + file.getName(), document);
		}
		executor.shutdown();
	}

	public void processFile(File file) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(file);
		processFiles(fileList);
	}
	
public class SentenceCallable implements Callable<IDocument> {
		
		private final IDocument sentenceDoc;
		private final IAnnotation<String> sentenceAnn;
		
		//TODO: Add doc position to prevent clashes!
		public SentenceCallable(IAnnotation<String> sentence){
			this.sentenceAnn = sentence;
			sentenceDoc = new Document(null);
			ArrayList<IAnnotation<String>> sentenceList = new ArrayList<IAnnotation<String>>();
			sentenceList.add(sentenceAnn);
			sentenceDoc.addSentence(AnnotatorEnum.SENTENCE.getAnnotator().getClass(), sentenceList);
		}

		public IDocument call() throws Exception {
			for(IAnnotator<?,?> annotator : config.getAnnotators()){
				try {
					ArrayList<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
					annotations.addAll(annotator.annotate(sentenceDoc.getAnnotations(AnnotatorEnum.SENTENCE.getAnnotator().getClass())));
					sentenceDoc.addAnnotations(annotator.getClass(), annotations);
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
				}
			}
			return sentenceDoc;
		}
		
	}
	
}

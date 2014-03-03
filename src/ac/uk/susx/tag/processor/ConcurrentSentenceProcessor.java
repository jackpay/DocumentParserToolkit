package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.SentenceAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentSentenceProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private static final Class<? extends IAnnotatorFactory<Sentence,String>> sentence = SentenceAnnotatorFactory.class;
	private final IConfiguration<CharSequence> config;
	
	public ConcurrentSentenceProcessor(IConfiguration<CharSequence> config){
		this.config = config;
	}

	public void processFiles(List<File> files) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for(File file : files){
			IDocument document = config.getDocumentBuilder().createDocument(file.getAbsolutePath());
			final ArrayList<Future<Void>> futures = new ArrayList<Future<Void>>();
			try {
				AnnotatorRegistry.getAnnotator(sentence).annotate(document);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Iterator<Sentence> sentences = document.getSentenceIterator();
			while(sentences.hasNext()){
				SentenceCallable sentCaller = new SentenceCallable(sentences.next());
				Future<Void> future = executor.submit(sentCaller);
				futures.add(future);
			}
			for(Future<Void> future : futures){
				try {
					@SuppressWarnings("unused")
					Void sent = future.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
public class SentenceCallable implements Callable<Void> {
		
		private final Sentence sentenceAnn;
		
		public SentenceCallable(Sentence sentence){
			this.sentenceAnn = sentence;
		}

		public Void call() throws Exception {
			for(IAnnotator<?,?> annotator : config.getAnnotators()){
				try {
					annotator.annotate(sentenceAnn);
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
	}
	
}

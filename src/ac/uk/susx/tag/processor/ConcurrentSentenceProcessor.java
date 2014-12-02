package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.SentenceAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ConcurrentSentenceProcessor implements IProcessor {
	
	private static final int NTHREADS = (Runtime.getRuntime().availableProcessors()) * 3;
	private static final Class<? extends IAnnotatorFactory<Sentence,String>> sentence = SentenceAnnotatorFactory.class;
	private final IConfiguration config;
	
	public ConcurrentSentenceProcessor(IConfiguration config){
		this.config = config;
	}

	public void processFiles(String fileDir) {
		final ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		Iterator<File> iter = FileUtils.iterateFiles(new File(fileDir), new String[] {config.getInputSuff()}, true);
		while(iter.hasNext()){
			File next = iter.next();
			Document document = config.getDocumentBuilder().createDocument(next.getAbsolutePath());
			final ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
			try {
				AnnotatorRegistry.getAnnotator(sentence).annotate(document);
			} catch (Exception e1) {
				e1.printStackTrace();
				break;
			}
			Iterator<Sentence> sentences = document.iterator();
			while(sentences.hasNext()){
				SentenceCallable sentCaller = new SentenceCallable(sentences.next());
				Future<Boolean> future = executor.submit(sentCaller);
				futures.add(future);
			}
			for(Future<Boolean> future : futures){
				try {
					if(future.get() == false) {
						break;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				} catch (ExecutionException e) {
					e.printStackTrace();
					break;
				}
			}
			document.retainDocumentAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
			document.filterDocumentAnnotations(config.getFilters()); // Remove the annotations specified by the filters.
			config.getOutputWriter().processDocument(config.getOutputLocation() + "/" + next.getName(), document);
		}
		executor.shutdown();
	}

	public void processFile(File file) {
		processFiles(file.getParentFile().getAbsolutePath());
	}
	
	public class SentenceCallable implements Callable<Boolean> {
			
			private final Sentence sentenceAnn;
			
			public SentenceCallable(Sentence sentence){
				this.sentenceAnn = sentence;
			}
	
			public Boolean call() throws Exception {
				for(IAnnotator<?,?> annotator : config.getAnnotators()){
					try {
						annotator.annotate(sentenceAnn);
					} catch (IncompatibleAnnotationException e) {
						e.printStackTrace();
						return false;
					}
				}
				return true;
			}
			
		}
		
	}

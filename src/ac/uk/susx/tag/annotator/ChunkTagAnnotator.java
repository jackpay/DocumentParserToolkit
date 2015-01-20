package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.InvalidFormatException;
import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.AnnotationUtils;

public final class ChunkTagAnnotator extends AbstractAnnotator<String,String>{

	private ChunkerME chunker;
	private static final String CHUNKSTART = "B-";
	private static final String INCHUNK = "I-";
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	private final Class<? extends IAnnotatorFactory<String,String>> postagger;
	
	public ChunkTagAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser, Class<? extends IAnnotatorFactory<String,String>> postagger) {
		this.postagger = postagger;
		this.tokeniser = tokeniser;
	}

	public synchronized List<Annotation<String>> annotate(Annotation<String> sentence) throws IncompatibleAnnotationException {
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		List<? extends Annotation<String>> tokens = null;
		try {
			tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<? extends Annotation<String>> postags = null;
		try {
			postags = AnnotatorRegistry.getAnnotator(postagger).annotate(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);
		String[] strTags = AnnotationUtils.annotationsToArray(postags, new String[postags.size()]);

		return chunk(strToks,strTags,sentence);
	}
	
	private synchronized List<Annotation<String>> chunk(final String[] toks, final String[] pos, final Annotation<String> sentence){
		
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		final String[] chunkTags = chunker.chunk(toks, pos);
		
		int begin = 0;
		for(int i = 0; i < chunkTags.length; i++){
			final Pattern pattern = Pattern.compile(Pattern.quote(toks[i]));
			final Matcher matcher = pattern.matcher(sentence.getAnnotation());
			matcher.find(begin);
			String chunk = chunkTags[i].replace(INCHUNK, "");
			chunk = chunk.replace(CHUNKSTART, "");
			StringAnnotation annotation = new StringAnnotation(chunk, sentence.getStart() + matcher.start(), sentence.getStart() + matcher.end());
			annotations.add(annotation);
			begin = matcher.end();
		}
		return annotations;
	}

	public synchronized List<Annotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		
		List<? extends Annotation<String>> tokens = null;
		List<? extends Annotation<String>> postags = null;
		
		try {
			tokens = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(tokeniser).getClass());
			postags = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(postagger).getClass());
		} catch (IllegalAnnotationStorageException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tokens == null){
			try {
				tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(postags == null){
			try {
				postags = AnnotatorRegistry.getAnnotator(postagger).annotate(sentence);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		final String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);
		final String[] strTags = AnnotationUtils.annotationsToArray(postags, new String[postags.size()]);	
		sentence.addAnnotations(this.getClass(), chunk(strToks,strTags,sentence.getSentence()));
		try {
			return sentence.getSentenceAnnotations(this.getClass());
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void startModel() {
		if(!modelStarted()){
			try {
				chunker = new ChunkerME(new ChunkerModel(this.getClass().getClassLoader().getResourceAsStream("enchunker.bin")));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean modelStarted() {
		return chunker != null;
	}

}

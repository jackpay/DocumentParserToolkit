package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class PorterStemmerAnnotator extends AbstractAnnotator<String,String> {

	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	private PorterStemmer stemmer = new PorterStemmer();
	
	public PorterStemmerAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser) {
		this.tokeniser = tokeniser;
	}

	/**
	 * Takes an annotation, assumed to be a sentence and uses the porter stemmer on it.
	 */
	public synchronized List<Annotation<String>> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException {
		List<? extends Annotation<String>> toks = null;
		try {
			toks = AnnotatorRegistry.getAnnotator(tokeniser).annotate(annotation);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<Annotation<String>> anns = new ArrayList<Annotation<String>>();
		for(Annotation<String> tok :  toks) {
			stemmer.reset();
			stemmer.add(tok.getAnnotation().toCharArray(), tok.getAnnotation().length());
			stemmer.stem();
			StringAnnotation token = new StringAnnotation(stemmer.toString(),tok.getStart(),tok.getEnd());
			anns.add(token);
		}
		return anns;
	}

	/**
	 * Not required.
	 */
	public void startModel() { }

	/**
	 * Always true as class is instantiated with the stemmer.
	 */
	public boolean modelStarted() {
		return true;
	}

	public List<Annotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<Annotation<String>> annos = annotate(sentence.getSentence());
		sentence.addAnnotations(this.getClass(), annos);
		return annos;
	}
}

package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;


public class PorterStemmerAnnotator extends AbstractStringAnnotator {
	
	private PorterStemmer stemmer = new PorterStemmer();

	/**
	 * Takes an annotation, assumed to be a sentence and uses the porter stemmer on it.
	 */
	public synchronized List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		List<? extends IAnnotation<String>> toks = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(annotation);
		ArrayList<StringAnnotation> anns = new ArrayList<StringAnnotation>();
		for(IAnnotation<String> tok :  toks) {
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
}
package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.token.Token;
import ac.uk.susx.tag.formatting.token.SentenceToken;

/**
 * Mainly used as a IAnnotation wrapper for the Sentence object.
 * @author jp242
 */
public class SentenceAnnotation extends Annotation<Sentence>{
	
	public SentenceAnnotation(Sentence annotation, int start, int end) {
		super(annotation, start, end);
	}

	@Override
	public String toString() {
		return this.getAnnotation().getSentence().getAnnotation();
	}

}

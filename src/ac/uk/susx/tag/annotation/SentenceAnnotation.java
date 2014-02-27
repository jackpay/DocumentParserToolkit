package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.IToken;
import ac.uk.susx.tag.formatting.SentenceToken;

/**
 * Mainly used as a wrapper for the Sentence object.
 * @author jp242
 *
 */
public class SentenceAnnotation extends AbstractAnnotation<Sentence>{
	
	private static final IToken<Sentence> formatter = new SentenceToken();

	public SentenceAnnotation(Sentence annotation, int start, int end) {
		super(annotation, start, end);
	}
	
	public CharSequence formatForOutput() {
		return formatter.formatForOutput(this);
	}

}

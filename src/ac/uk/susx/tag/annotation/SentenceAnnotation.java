package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.document.Sentence;

/**
 * Mainly used as a wrapper for the Sentence object.
 * @author jp242
 *
 */
public class SentenceAnnotation extends AbstractAnnotation<Sentence>{

	public SentenceAnnotation(Sentence annotation, int start, int end) {
		super(annotation, start, end);
	}

}

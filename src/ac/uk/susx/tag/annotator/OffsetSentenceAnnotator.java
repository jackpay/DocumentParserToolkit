package ac.uk.susx.tag.annotator;

/**
 * A sentence annotator which DOES NOT STORE the sentence string in the annotation object. A null object is passed instead and the sentence string offset values are intended to be used instead.
 * @author jp242
 *
 */
public class OffsetSentenceAnnotator extends AbstractSentenceAnnotator {

	public OffsetSentenceAnnotator() {
		super(false);
	}

}

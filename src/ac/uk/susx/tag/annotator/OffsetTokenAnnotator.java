package ac.uk.susx.tag.annotator;

/**
 * A token annotator which DOES NOT STORE the token string in the annotation object. A null object is passed instead and the token string offset values are intended to be used instead.
 * @author jp242
 *
 */
public class OffsetTokenAnnotator extends AbstractTokenAnnotator {

	public OffsetTokenAnnotator() {
		super(false);
	}

}

package ac.uk.susx.tag.indexing;

import ac.uk.susx.tag.annotator.factory.AbstractAnnotatorFactory;

public class AnnotatorIndexToken {
	
	private final AbstractAnnotatorFactory factory;
	
	private AnnotatorIndexToken(AbstractAnnotatorFactory factory) {
		this.factory = factory;
	}

	public static final AnnotatorIndexToken generateIndexToken(AbstractAnnotatorFactory factory) {
		return new AnnotatorIndexToken(factory);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AnnotatorIndexToken) {
			AnnotatorIndexToken comp = (AnnotatorIndexToken) obj;
			return comp.factory == factory;
		}
		return false;
	}

}

package ac.uk.susx.tag.indexing;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

public class AnnotatorIndexToken {
	
	private final IAnnotatorFactory factory;
	
	private AnnotatorIndexToken(IAnnotatorFactory factory) {
		this.factory = factory;
	}

	public static final AnnotatorIndexToken generateIndexToken(IAnnotatorFactory factory) {
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

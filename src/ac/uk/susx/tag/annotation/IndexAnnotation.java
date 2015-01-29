package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IndexToken;

public class IndexAnnotation<A extends IndexToken> extends Annotation<A> {

	public IndexAnnotation(A annotation, int start, int end) {
		super(annotation, start, end);
	}

	@Override
	public String toString() {
		return this.getAnnotation().toString();
	}

}

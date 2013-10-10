package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IIndexToken;


public class StringAnnotation extends AbstractAnnotation<String> {

	public StringAnnotation(String annotation, int start, int end) {
		super(annotation, start, end);
	}
	
}

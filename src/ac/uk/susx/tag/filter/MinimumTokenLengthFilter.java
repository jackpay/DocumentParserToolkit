package ac.uk.susx.tag.filter;

import ac.uk.susx.tag.annotator.IAnnotator;

public class MinimumTokenLengthFilter extends AbstractAnnotationFilter<String>{
	
	public final int len;

	public MinimumTokenLengthFilter(Class<? extends IAnnotator> annotator,boolean remAllTok, int minLength) {
		super(annotator, remAllTok, true);
		this.len = minLength;
	}

	@Override
	public boolean matchAnnotation(String annotation) {
		if(annotation.length() < len) {
			return true;
		}
		return false;
	}

}

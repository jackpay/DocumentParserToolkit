package ac.uk.susx.tag.filter;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class MinimumTokenLengthFilter extends AbstractAnnotationFilter<String>{
	
	public final int len;

	public MinimumTokenLengthFilter(Class<? extends IAnnotator> annotator,boolean remAllTok, int minLength) {
		super(annotator, remAllTok, true);
		this.len = minLength;
	}

	@Override
	public boolean matchAnnotation(IAnnotation<String> annotation) {
		if(annotation.getAnnotation().length() < len) {
			return true;
		}
		return false;
	}

}

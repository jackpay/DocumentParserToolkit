package ac.uk.susx.tag.filter;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class EmptyAnnotationFilter extends AbstractAnnotationFilter<String>{

	public EmptyAnnotationFilter(Class<? extends IAnnotator> annotator) {
		super(annotator, true, true);
	}

	@Override
	public boolean matchAnnotation(IAnnotation<String> annotation) {
		if(annotation.getAnnotation() == null || annotation.getAnnotation().equals("")) {
			return true;
		}
		return false;
	}

}

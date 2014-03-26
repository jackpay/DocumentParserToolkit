package ac.uk.susx.tag.filter;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class RetainAnnotationFilter<AT> extends AbstractAnnotationFilter<AT> {

	public RetainAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, false);
	}
	
	public RetainAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, false);
	}
	
	public boolean matchAnnotation(IAnnotation<AT> annotation){
		boolean match = false;
		for(AT exAnn : getFilterAnnotations()){
			if(annotation.getAnnotation().equals(exAnn)){
				return true;
			}
		}
		return match;
	}

}

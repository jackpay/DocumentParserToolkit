package ac.uk.susx.tag.filter;

import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class RemoveAnnotationFilter<AT> extends AbstractAnnotationFilter<AT>{

	public RemoveAnnotationFilter(AT annotation, Class<? extends IAnnotator<AT,?>> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, true);
	}
	
	public RemoveAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator<AT,?>> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, true);
	}

	@Override
	public boolean matchAnnotation(Annotation<AT> annotation){
		boolean match = false;
		for(AT exAnn : getFilterAnnotations()){
			if(annotation.getAnnotation().equals(exAnn)){
				return true;
			}
		}
		return match;
	}

}

package ac.uk.susx.tag.filter;

import java.util.List;

import ac.uk.susx.tag.annotator.IAnnotator;

public class RemoveAnnotationFilter<AT> extends AbstractAnnotationFilter<AT>{

	public RemoveAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, true);
	}
	
	public RemoveAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, true);
	}
	
	public boolean matchAnnotation(AT annotation){
		boolean match = false;
		for(AT exAnn : getFilterAnnotations()){
			if(annotation.equals(exAnn)){
				return true;
			}
		}
		return match;
	}

}

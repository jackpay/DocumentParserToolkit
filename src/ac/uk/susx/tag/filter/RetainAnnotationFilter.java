package ac.uk.susx.tag.filter;

import java.util.List;

import ac.uk.susx.tag.annotator.IAnnotator;

public class RetainAnnotationFilter<AT> extends AbstractAnnotationFilter<AT> {

	public RetainAnnotationFilter(AT annotation, Class<? extends IAnnotator<AT,?,?>> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, false);
	}
	
	public RetainAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator<AT,?,?>> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, false);
	}

}

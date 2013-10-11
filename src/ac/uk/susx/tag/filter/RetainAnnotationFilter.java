package ac.uk.susx.tag.filter;

import java.util.Collection;

import ac.uk.susx.tag.annotator.IAnnotator;

public class RetainAnnotationFilter<AT> extends AbstractAnnotationFilter<AT> {

	public RetainAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, false);
	}
	
	public RetainAnnotationFilter(Collection<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, false);
	}

}

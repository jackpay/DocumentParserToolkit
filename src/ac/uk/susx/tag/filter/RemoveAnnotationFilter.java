package ac.uk.susx.tag.filter;

import java.util.Collection;

import ac.uk.susx.tag.annotator.IAnnotator;

public class RemoveAnnotationFilter<AT> extends AbstractAnnotationFilter<AT>{

	public RemoveAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(annotation, annotator, remAllTok, true);
	}
	
	public RemoveAnnotationFilter(Collection<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok) {
		super(filterAnnotations, annotator, remAllTok, true);
	}

}

package ac.uk.susx.tag.annotation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.utils.FilterUtils;

public class SentenceAnnotation extends AbstractAnnotation <Map<Class<? extends IAnnotator<?,?,?>>, List<IAnnotation<?>>>> {

	public SentenceAnnotation(int start, int end) {
		super(new HashMap<Class<? extends IAnnotator<?,?,?>>, List<IAnnotation<?>>>(), start, end);
	}
	
	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator, List<? extends IAnnotation<AT>> annotations) {
		getAnnotation().put(annotator, annotations);
		sortAnnotations(annotator);
	}
	
	public <IT> List<IAnnotation<IT>> getAnnotations(Class<? extends IAnnotator<IT,?,?>> annotator){
		return getAnnotation().get(annotator).getClass().cast(getAnnotation().get(annotator));
	}
	
	private void sortAnnotations(Class<? extends IAnnotator<?,?,?>> annotator){
		Collections.sort(getAnnotation().get(annotator), new FilterUtils.AnnotationOffsetComparator());
	}

}

package ac.uk.susx.tag.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.utils.FilterUtils;

public class SentenceAnnotation extends AbstractAnnotation <Map<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>> {

	public SentenceAnnotation(int start, int end) {
		super(new HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>(), start, end);
	}
	
	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator, List<? extends IAnnotation<AT>> annotations) {
		List<IAnnotation<AT>> anns = (List<IAnnotation<AT>>) getAnnotation().get(annotator);
		if(anns == null){
			getAnnotation().put(annotator, new ArrayList<IAnnotation<AT>>());
			anns = (List<IAnnotation<AT>>) getAnnotation().get(annotator);
			anns.addAll(annotations);
		}
		else{
			anns.addAll(annotations);
		}
		sortAnnotations(annotator);
	}
	
	public <IT> List<IAnnotation<IT>> getAnnotations(Class<? extends IAnnotator<IT,?,?>> annotator){
		return getAnnotation().get(annotator).getClass().cast(getAnnotation().get(annotator));
	}
	
	private <AT> List<IAnnotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator){
		Collections.sort(getAnnotation().get(annotator), new FilterUtils.AnnotationOffsetComparator());
		return getAnnotation().get(annotator).getClass().cast(getAnnotation().get(annotator));
	}

}

package ac.uk.susx.tag.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotator.IAnnotator;

public class SentenceAnnotation extends AbstractAnnotation <Map<Class<? extends IAnnotator>, List<IAnnotation>>> {

	public SentenceAnnotation(int start, int end) {
		super(new HashMap<Class<? extends IAnnotator>, List<IAnnotation>>(), start, end);
	}
	
	

}

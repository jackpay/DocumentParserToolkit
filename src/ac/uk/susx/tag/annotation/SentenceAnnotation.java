package ac.uk.susx.tag.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.utils.FilterUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public final class SentenceAnnotation<A> extends AbstractAnnotation <Map<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>> {
	
	private final IAnnotation<A> sentence;

	public SentenceAnnotation(IAnnotation<A> sentence, int start, int end) {
		super(new HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>(), start, end);
		this.sentence = sentence;
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
		try {
			sortAnnotations(annotator);
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
	}
	
	public <IT> List<IAnnotation<IT>> getAnnotations(Class<? extends IAnnotator<IT,?,?>> annotator) throws IllegalAnnotationStorageException{
		try{
			return getAnnotation().get(annotator).getClass().cast(getAnnotation().get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	public IAnnotation<A> getSentence() {
		return sentence;
	}
	
	private <AT> List<IAnnotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator) throws IllegalAnnotationStorageException{
		Collections.sort(getAnnotation().get(annotator), new FilterUtils.AnnotationOffsetComparator());
		try{
			return getAnnotation().get(annotator).getClass().cast(getAnnotation().get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}

}

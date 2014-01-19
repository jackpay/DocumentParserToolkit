package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.AbstractAnnotation;
import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.utils.FilterUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public final class Sentence<A> {
	
	private final IAnnotation<A> sentence;
	private final HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>> annotations;

	public Sentence(IAnnotation<A> sentence, int start, int end) {
		//super(new HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>(), start, end);
		annotations = new HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>();
		this.sentence = sentence;
	}
	
	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator, List<? extends IAnnotation<AT>> annos) {
		List<IAnnotation<AT>> anns = (List<IAnnotation<AT>>) annotations.get(annotator);
		if(anns == null){
			annotations.put(annotator, new ArrayList<IAnnotation<AT>>());
			anns = (List<IAnnotation<AT>>) annotations.get(annotator);
			anns.addAll(annos);
		}
		else{
			anns.addAll(annos);
		}
		try {
			sortAnnotations(annotator);
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
	}
	
	public <IT> List<IAnnotation<IT>> getAnnotations(Class<? extends IAnnotator<IT,?,?>> annotator) throws IllegalAnnotationStorageException{
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	public IAnnotation<A> getSentence() {
		return sentence;
	}
	
	public Collection<List<? extends IAnnotation<?>>> getSentenceAnnotations() {
		return annotations.values();
	}
	
	public void removeAnnotation(Class<? extends IAnnotator<?,?,?>> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}

	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> annotators) {
		for(Class<? extends IAnnotator<?,?,?>> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
	
	public void filterAnnotations(Collection<IFilter<?>> filters){
		if(filters != null && !filters.isEmpty()){
			for(IFilter<?> filter : filters){
				filter.filterCollection(annotations);
			}
		}
	}
	
	//TODO: type checking - although is enforced elsewhere 
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?,?>> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filter((List<? extends IAnnotation<AT>>) annotations.get(annotator));
			}
		}
	}
	
	private <AT> List<IAnnotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?,?>> annotator) throws IllegalAnnotationStorageException{
		Collections.sort(annotations.get(annotator), new FilterUtils.AnnotationOffsetComparator());
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}

}

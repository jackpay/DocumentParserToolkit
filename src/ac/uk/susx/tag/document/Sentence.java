package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.indexing.OffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public final class Sentence {
	
	private final IAnnotation<String> sentence;
	private final HashMap<Class<? extends IAnnotator<?,?>>, List<? extends IAnnotation<?>>> annotations;
	private final HashMap<OffsetIndexToken, List<? extends IAnnotation<?>>> indexAnnotations;
	private final OffsetIndexToken offset;
	private final CharSequence docId;

	public Sentence(IAnnotation<String> sentence, int start, int end) {
		annotations = Maps.newHashMap();
		indexAnnotations = Maps.newHashMap();
		this.sentence = sentence;
		this.offset = new OffsetIndexToken(start,end);
		docId = null;
	}
	
	public Sentence(IAnnotation<String> sentence, int start, int end, CharSequence docId) {
		annotations = Maps.newHashMap();
		indexAnnotations = Maps.newHashMap();
		this.sentence = sentence;
		this.offset = new OffsetIndexToken(start,end);
		this.docId = docId;
	}
	
	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?>> annotator, List<? extends IAnnotation<AT>> annos) {
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
		
		for(IAnnotation<AT> annotation : annos) {
			try {
				if(indexAnnotations.get(annotation.getOffset()) == null){
					indexAnnotations.put(annotation.getOffset(), new ArrayList<IAnnotation<AT>>(Arrays.asList(annotation)));
				}
				else {
					List<IAnnotation<?>> list = (List<IAnnotation<?>>) indexAnnotations.get(annotation.getOffset());
					list.add(annotation);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<? extends IAnnotation<?>> getIndexedAnnotations(OffsetIndexToken index) {
		return indexAnnotations.get(index);
	}
	
	public Set<OffsetIndexToken> getAnnotationIndexes() {
		return indexAnnotations.keySet();
	} 
	
	public Collection<List<? extends IAnnotation<?>>> getAllIndexedAnnotations() {
		return indexAnnotations.values();
	}
	
	public <AT> List<IAnnotation<AT>> getSentenceAnnotations(Class<? extends IAnnotator<AT,?>> annotator) throws IllegalAnnotationStorageException{
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	public IAnnotation<String> getSentence() {
		return sentence;
	}
	
	public Collection<List<? extends IAnnotation<?>>> getSentenceAllAnnotations() {
		return annotations.values();
	}
	
	public Set<Class<? extends IAnnotator<?, ?>>> getSentenceAllAnnotators() {
		return annotations.keySet();
	}
	
	public void removeAnnotation(Class<? extends IAnnotator<?,?>> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}
	
	public boolean removeAnnotation(OffsetIndexToken index) {
		if(indexAnnotations.containsKey(index)) {
			indexAnnotations.remove(index);
			return true;
		}
		return false;
	}

	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?>>> annotators) {
		for(Class<? extends IAnnotator<?,?>> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?>>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
	
	public void filterAnnotations(Collection<IFilter<?>> filters){
		if(filters != null && !filters.isEmpty()){
			for(IFilter<?> filter : filters){
				filter.filterSentence(this);
			}
		}
	}
	
	//TODO: type checking - although is enforced elsewhere 
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?>> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filterList((List<? extends IAnnotation<AT>>) annotations.get(annotator));
			}
		}
	}
	
	private <AT> List<IAnnotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?>> annotator) throws IllegalAnnotationStorageException{
		Collections.sort(annotations.get(annotator), new FilterUtils.AnnotationOffsetComparator());
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	public OffsetIndexToken getOffsetIndex() {
		return offset;
	}
	
	public CharSequence docReference() {
		return docId;
	}

}

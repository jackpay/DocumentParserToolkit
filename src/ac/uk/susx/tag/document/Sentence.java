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

/**
 * The class used to represent a single sentence. Stores and maintains and index of all tokens and annotations.
 * @author jp242
 */
public final class Sentence {
	
	private final IAnnotation<String> sentence;
	private final HashMap<Class<? extends IAnnotator<?,?>>, List<? extends IAnnotation<?>>> annotations;
	private final HashMap<OffsetIndexToken, List<? extends IAnnotation<?>>> indexAnnotations;
	private final OffsetIndexToken offset;
	private final CharSequence docId;

	/**
	 * @param sentence The raw sentence string.
	 * @param start The index of the sentence start point in the document.
	 * @param end  The index of the sentence end point in the document.
	 */
	public Sentence(IAnnotation<String> sentence, int start, int end) {
		annotations = Maps.newHashMap();
		indexAnnotations = Maps.newHashMap();
		this.sentence = sentence;
		this.offset = new OffsetIndexToken(start,end);
		docId = null;
	}
	
	/**
	 * @param sentence The raw sentence string.
	 * @param start The index of the sentence start point in the document.
	 * @param end  The index of the sentence end point in the document.
	 * @param docId The id of the document the senetence comes from.
	 */
	public Sentence(IAnnotation<String> sentence, int start, int end, CharSequence docId) {
		annotations = Maps.newHashMap();
		indexAnnotations = Maps.newHashMap();
		this.sentence = sentence;
		this.offset = new OffsetIndexToken(start,end);
		this.docId = docId;
	}
	
	/**
	 * @param annotator The class of the annotator which produced the annotations.
	 * @param annos The annotations to be indexed.
	 */
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
	
	/**
	 * @param index The index of the annotations required.
	 * @return All annotations associated with the given index.
	 */
	public List<? extends IAnnotation<?>> getIndexedAnnotations(OffsetIndexToken index) {
		return indexAnnotations.get(index);
	}
	
	/**
	 * @return All index tokens contained in the Sentence.
	 */
	public Set<OffsetIndexToken> getAnnotationIndexes() {
		return indexAnnotations.keySet();
	} 
	
	/**
	 * @return All indexed annotations contained in the Sentence.
	 */
	public Collection<List<? extends IAnnotation<?>>> getAllIndexedAnnotations() {
		return indexAnnotations.values();
	}
	
	/**
	 * @param annotator The class of the annotator which produces the required annotations.
	 * @return All sentence annotations produced by the given annotator class, or null.
	 * @throws IllegalAnnotationStorageException
	 */
	public <AT> List<IAnnotation<AT>> getSentenceAnnotations(Class<? extends IAnnotator<AT,?>> annotator) throws IllegalAnnotationStorageException {
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	/**
	 * @return The raw sentence annotation represented by the Sentence object.
	 */
	public IAnnotation<String> getSentence() {
		return sentence;
	}
	
	/**
	 * @return All annotations contained in this Sentence.
	 */
	public Collection<List<? extends IAnnotation<?>>> getSentenceAllAnnotations() {
		return annotations.values();
	}
	
	/**
	 * @return A set of all classes which have annotations contained in the Sentence.
	 */
	public Set<Class<? extends IAnnotator<?, ?>>> getSentenceAllAnnotators() {
		return annotations.keySet();
	}
	
	/**
	 * @param cl Removes all annotations associated with the given class.
	 */
	public void removeAnnotation(Class<? extends IAnnotator<?,?>> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}
	
	/**
	 * @param index The index of the annotations needing to be removed.
	 * @return returns true if the Sentence contained annotations at the given index.
	 */
	public boolean removeAnnotation(OffsetIndexToken index) {
		if(indexAnnotations.containsKey(index)) {
			indexAnnotations.remove(index);
			return true;
		}
		return false;
	}

	/**
	 * @param annotators Removes all annotations associated with the given collection of annotators.
	 */
	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?>>> annotators) {
		for(Class<? extends IAnnotator<?,?>> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	/**
	 * @param includedAnnotators  Retains all annotations associated with the given collection of annotators and removes all others.
	 */
	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?>>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
	
	/**
	 * @param filters Applies the given filters to the annotations of the Sentence.
	 */
	public void filterAnnotations(Collection<IFilter<?>> filters){
		if(filters != null && !filters.isEmpty()){
			for(IFilter<?> filter : filters){
				filter.filterSentence(this);
			}
		}
	}
	
	//TODO: type checking - although is enforced elsewhere 
	/**
	 * Apply the given set of filters to all annotations created by the given class.
	 * @param filters 
	 * @param annotator
	 */
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?>> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filterList((List<? extends IAnnotation<AT>>) annotations.get(annotator));
			}
		}
	}
	
	/**
	 * Used to sort the annotations in ascending order of offset.
	 * @param annotator
	 * @return
	 * @throws IllegalAnnotationStorageException
	 */
	private <AT> List<IAnnotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?>> annotator) throws IllegalAnnotationStorageException{
		Collections.sort(annotations.get(annotator), new FilterUtils.AnnotationOffsetComparator());
		try{
			return annotations.get(annotator).getClass().cast(annotations.get(annotator));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(annotator.getClass());
		}
	}
	
	/**
	 * @return The offset of this sentence in relation to its document.
	 */
	public OffsetIndexToken getOffsetIndex() {
		return offset;
	}
	
	/**
	 * @return The doc id this sentence comes from (if one exists).
	 */
	public CharSequence docReference() {
		return docId;
	}

}

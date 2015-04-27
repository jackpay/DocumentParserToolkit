package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.indexing.OffsetIndexToken;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

/**
 * The class used to represent a single sentence. Stores and maintains and index of all tokens and annotations.
 * @author jp242
 */
public final class Sentence extends HashMap<Class<IAnnotator<?,?>>, List<Annotation<?>>> {
	
	private static final long serialVersionUID = -2695404603211419648L;
	private final Annotation<String> sentence;
	private final HashMap<Class<? extends IAnnotator<?,?>>, List<? extends Annotation<?>>> annotations;
	private final HashMap<OffsetIndexToken, List<Annotation<?>>> indexAnnotations;
	private final OffsetIndexToken offset;

	/**
	 * @param sentence The raw sentence string.
	 * @param start The index of the sentence start point in the document.
	 * @param end  The index of the sentence end point in the document.
	 */
	public Sentence(Annotation<String> sentence, int start, int end) {
		annotations = Maps.newHashMap();
		indexAnnotations = Maps.newHashMap();
		this.sentence = sentence;
		this.offset = new OffsetIndexToken(start,end);
	}
	
	/**
	 * @param annotator The class of the annotator which produced the annotations.
	 * @param annos The annotations to be indexed.
	 */
	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?>> annotator, List<? extends Annotation<AT>> annos) {
		annotations.put(annotator, annos);
		try {
			sortAnnotations(annotator);
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		
		for(Annotation<AT> annotation : annos) {
			try {
				if(indexAnnotations.get(annotation.getOffset()) == null){
					indexAnnotations.put(annotation.getOffset(), new ArrayList<Annotation<?>>(Arrays.asList(annotation)));
				}
				else {
					List<Annotation<?>> list = (List<Annotation<?>>) indexAnnotations.get(annotation.getOffset());
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
	public List<Annotation<?>> getIndexedAnnotations(OffsetIndexToken index) {
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
	public Collection<List<Annotation<?>>> getAllIndexedAnnotations() {
		return indexAnnotations.values();
	}
	
	/**
	 * @param class1 The class of the annotator which produces the required annotations.
	 * @return All sentence annotations produced by the given annotator class, or null.
	 * @throws IllegalAnnotationStorageException
	 */
	public <AT> List<Annotation<AT>> getSentenceAnnotations(Class<? extends IAnnotator<AT,?>> class1) throws IllegalAnnotationStorageException {
		if(annotations.get(class1) == null) {
			return null;
		}
		try{
			return annotations.get(class1).getClass().cast(annotations.get(class1));
		} catch (ClassCastException ex) {
			throw new IllegalAnnotationStorageException(class1.getClass());
		}
	}
	
	/**
	 * @return The raw sentence annotation represented by the Sentence object.
	 */
	public Annotation<String> getSentence() {
		return sentence;
	}
	
	/**
	 * @return All annotations contained in this Sentence.
	 */
	public Collection<List<? extends Annotation<?>>> getSentenceAllAnnotations() {
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
	public void removeAnnotation(Class<? extends IAnnotator> cl) {
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
		for(Class<? extends IAnnotator> annotator : annotators){
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
	
	/**
	 * Apply the given set of filters to all annotations created by the given class.
	 * @param filters 
	 * @param annotator
	 */
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?>> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filterList((List<? extends Annotation<AT>>) annotations.get(annotator));
			}
		}
	}
	
	/**
	 * Used to sort the annotations in ascending order of offset.
	 * @param annotator
	 * @return
	 * @throws IllegalAnnotationStorageException
	 */
	private <AT> List<Annotation<AT>> sortAnnotations(Class<? extends IAnnotator<AT,?>> annotator) throws IllegalAnnotationStorageException{
		Collections.sort(annotations.get(annotator), new AnnotationUtils.AnnotationOffsetComparator());
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
	
	public String toString() {
		return sentence.getAnnotation();
	}

}

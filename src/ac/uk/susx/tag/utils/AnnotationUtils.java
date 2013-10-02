package ac.uk.susx.tag.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;

public class AnnotationUtils {
	
	public static <A> A[] annotationsToArray(Collection<? extends Annotation<A>> annotations, A[] array) throws Exception {
		if(array.length != annotations.size()){
			throw new Exception("The array must be the same length as the collection.");
		}
		int i = 0;
		for(Annotation<A> annotation : annotations){
			array[i] = annotation.getAnnotation();
			i++;
		}
		return (A[]) array;
	}
	
	public static <AT> ArrayList<Annotation<AT>> annotationsToSortedArrayList(Map<IndexToken, ? extends Annotation<AT>> annotations){
		ArrayList<Annotation<AT>> anns = new ArrayList<Annotation<AT>>(annotations.values());
		Collections.sort(anns, new FilterUtils.AnnotationOffsetComparator());
		return anns;
	}
	
	public static <AT> Map<IndexToken, Collection<Annotation<AT>>> collateAnnotations(Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotations){
		Map<IndexToken, Collection<Annotation<AT>>> collectedAnnotations = new HashMap<IndexToken, Collection<Annotation<AT>>>(annotations.size()+((int)annotations.size()/4));
		for(Map<IndexToken, Annotation<AT>> annotator : annotations.values()){
				for(IndexToken index : annotator.keySet()){
					if(collectedAnnotations.get(index) == null){
						collectedAnnotations.put(index, new ArrayList<Annotation<AT>>());
						collectedAnnotations.get(index).add(annotator.get(index));
					}
					else{
						collectedAnnotations.get(index).add(annotator.get(index));
					}
				}
		}
		return collectedAnnotations;
	}
	
}

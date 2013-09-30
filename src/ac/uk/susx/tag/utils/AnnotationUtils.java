package ac.uk.susx.tag.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;

public class AnnotationUtils {
	
	public static <A> A[] annotationsToArray(Collection<? extends Annotation<A>> annotations, A[] array){
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
	
	public static <AT> Map<IndexToken, Collection<Annotation<AT>>> collateAnnotations(Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations){
		Map<IndexToken, Collection<Annotation<AT>>> collectedAnnotations = new HashMap<IndexToken, Collection<Annotation<AT>>>(annotations.size()+((int)annotations.size()/4));
		for(Class<? extends Annotator> annotator : annotations.keySet()){
				for(Annotation<AT> ann : annotations.get(annotator)){
					if(collectedAnnotations.get(ann.getOffset()) == null){
						collectedAnnotations.put(ann.getOffset(), new ArrayList<Annotation<AT>>());
						collectedAnnotations.get(ann.getOffset()).add(ann);
					}
					else{
						collectedAnnotations.get(ann.getOffset()).add(ann);
					}
				}
		}
		return collectedAnnotations;
	}
	
	
	
}

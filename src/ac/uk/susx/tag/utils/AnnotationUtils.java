package ac.uk.susx.tag.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;

public class AnnotationUtils {
	
	public static <A> A[] annotationsToArray(Collection<? extends IAnnotation<A>> annotations, A[] array){
		Iterator<? extends IAnnotation<A>> iter = annotations.iterator();
		int i = 0;
		while(iter.hasNext()){
			array[i] = (A) iter.next().getAnnotation();
			i++;
		}
		return (A[]) array;
	}
	
	public static <AT> Map<IIndexToken, Collection<IAnnotation<AT>>> collateAnnotations(Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> annotations, Collection<Class<? extends IAnnotator>> annotators){
		Map<IIndexToken, Collection<IAnnotation<AT>>> collectedAnnotations = new HashMap<IIndexToken, Collection<IAnnotation<AT>>>(annotations.size()+((int)annotations.size()/4));
		for(Class<? extends IAnnotator> annotator : annotators){
				for(IAnnotation<AT> ann : annotations.get(annotator)){
					if(collectedAnnotations.get(ann.getOffset()) == null){
						collectedAnnotations.put(ann.getOffset(), new ArrayList<IAnnotation<AT>>());
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

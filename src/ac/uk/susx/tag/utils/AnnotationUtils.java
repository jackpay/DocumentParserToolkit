package ac.uk.susx.tag.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;

public class AnnotationUtils {
	
	public static <A> A[] annotationsToArray(List<? extends IAnnotation<A>> annotations, A[] array){
		Iterator<? extends IAnnotation<A>> iter = annotations.iterator();
		int i = 0;
		while(iter.hasNext()){
			array[i] = (A) iter.next().getAnnotation();
			i++;
		}
		return (A[]) array;
	}
	
//	public static Map<IIndexToken, List<IAnnotation<?>>> collateAnnotations(Map<Class<? extends IAnnotator<?,?>>, List<IAnnotation<?>>> annotations, List<Class<? extends IAnnotator<?,?>>> annotators){
//		Map<IIndexToken, List<IAnnotation<?>>> collectedAnnotations = new HashMap<IIndexToken, List<IAnnotation<?>>>(annotations.size()+((int)annotations.size()/4));
//		for(Class<? extends IAnnotator<?,?>> annotator : annotators){
//				for(IAnnotation<?> ann : annotations.get(annotator)){
//					try {
//						if(collectedAnnotations.get(ann.getIndexToken(TermOffsetIndexToken.class)) == null){
//							collectedAnnotations.put(ann.getIndexToken(TermOffsetIndexToken.class), new ArrayList<IAnnotation<?>>());
//							collectedAnnotations.get(ann.getIndexToken(TermOffsetIndexToken.class)).add(ann);
//						}
//						else{
//							collectedAnnotations.get(ann.getIndexToken(TermOffsetIndexToken.class)).add(ann);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//		}
//		return collectedAnnotations;
//	}
	
}

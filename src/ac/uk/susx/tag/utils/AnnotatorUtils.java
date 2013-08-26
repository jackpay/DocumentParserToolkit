package ac.uk.susx.tag.utils;


import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.Annotation;

public class AnnotatorUtils {
	
	public static <A> A[] annotationsToArray(Collection<? extends Annotation<A>> annotations, A[] array){
		Iterator<? extends Annotation<A>> iter = annotations.iterator();
		int i = 0;
		while(iter.hasNext()){
			array[i] = (A) iter.next().getAnnotation();
			i++;
		}
		return (A[]) array;
	}
}

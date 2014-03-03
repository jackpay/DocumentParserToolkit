package ac.uk.susx.tag.utils;

import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;

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
	
}

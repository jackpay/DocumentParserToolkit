package ac.uk.susx.tag.annotation.annotator;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;

public class AnnotatorUtils {
	
	public static <A, T> A[] annotationsToArray(Collection<? extends Annotation> annotations){
		Annotation<A,T>[] anns = (Annotation<A,T>[]) annotations.toArray();
		A[] rawAnnotations = (A[]) new Object[anns.length];
		for(int i = 0; i < anns.length; i++){
			rawAnnotations[i] = anns[i].getAnnotation();
		}
		return (A[]) rawAnnotations;
	}
}

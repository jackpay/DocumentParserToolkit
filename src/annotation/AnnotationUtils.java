package annotation;

import java.util.Collection;

public class AnnotationUtils <T>{
	
	public T[] annotationsToArray(Collection<Annotation> annotations){
		Annotation[] anns = (Annotation[]) annotations.toArray();
		Object[] rawAnnotations = new Object[anns.length];
		for(int i = 0; i < anns.length; i++){
			rawAnnotations[i] = anns[i].getAnnotation();
		}
		return (T[]) rawAnnotations;
	}

}

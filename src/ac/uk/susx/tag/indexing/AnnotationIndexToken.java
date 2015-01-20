package ac.uk.susx.tag.indexing;

import ac.uk.susx.tag.annotation.Annotation;

/**
 * An index token based on the annotation.
 * @author jp242
 *
 * @param <A>
 */
public class AnnotationIndexToken <A> extends IndexToken {

	private final A annotation;
	
	public AnnotationIndexToken(A annotation){
		this.annotation = annotation;
	}
	
	/**
	 * Return the Annotation object
	 * @return
	 */
	public A getAnnotation(){
		return annotation;
	}
	
	public int hashCode(){
		int prime = 3;
		int hash = prime * annotation.hashCode();
		return hash;
	}
	
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(obj == this){
			return true;
		}
		if(!(obj instanceof Annotation<?>)){
			return false;
		}
		else{
			Annotation<?> ann = (Annotation<?>) obj;
			if(!(ann.getAnnotation().getClass() == this.getAnnotation().getClass())){
				return false;
			}
			else {
				if(ann.getAnnotation().equals(this.getAnnotation())){
					return true;
				}
			}
		}
		return false;
	}

}

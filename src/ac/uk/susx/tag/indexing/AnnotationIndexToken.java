package ac.uk.susx.tag.indexing;

/**
 * An index token based on the annotation.
 * @author jp242
 *
 * @param <A>
 */
public class AnnotationIndexToken <A> implements IndexToken {

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
	
	/**
	 * Equals method. Used for indexing. WARNING: for correct use, the equals method of custom annotation classes must be specified
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == null){
			return false;
		}
		if(obj.getClass() != this.getClass()){
			return false;
		}
		if(obj.getClass() == this.getClass()){
			AnnotationIndexToken pos = (AnnotationIndexToken) obj;
			if(pos.annotation.getClass() == this.annotation.getClass()){
				if(this.annotation.equals(pos.annotation)){
					return true;
				}
			}
		}
		return false;
	}
}

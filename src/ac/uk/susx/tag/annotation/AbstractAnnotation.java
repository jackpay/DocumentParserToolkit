package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 *
 */
public abstract class AbstractAnnotation<A> implements Annotation<A>{
	
	private final AnnotationIndexToken<A> annotation;
	private final TermOffsetIndexToken offset;
	
	public AbstractAnnotation(A annotation, int start, int end){
		this.annotation = new AnnotationIndexToken<A>(annotation);
		offset = new TermOffsetIndexToken(start,end);
	}
	
	public int getStart(){
		return offset.getStart();
	}
	
	public int getEnd(){
		return offset.getEnd();
	}
	
	public A getAnnotation(){
		return annotation.getAnnotation();
	}
	
	//TODO: Create a better annotation hash code.
	public int annotationHashCode(){
		return annotation.hashCode();
	}
	
	public IndexToken getPositionIndex() {
		return offset;
	}

//	
//	//TODO: Create hash code translator for continuous recall questions.
//	// Keep it here or in Filter?
//	public int hashCodeTranslator(int currentHash){
//		return 0;
//	}
//	
//	/**
//	 * Used for indexing the Annotation object by its annotation field.
//	 */
//	public boolean equals(Object obj){
//		if(obj == null){
//			return false;
//		}
//		if(obj.getClass() != this.getClass()){
//			return false;
//		}
//		if(obj.getClass() == this.getClass()){
//			AbstractAnnotation<A> ann = (AbstractAnnotation<A>) obj;
//			if(this.annotation.equals(ann.annotation)){
//				return true;
//			}
//		}
//		return false;
//	}
//	
}

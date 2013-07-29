package ac.uk.susx.tag.annotation;

import java.io.IOException;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 *
 */
public abstract class AbstractAnnotation<A> implements Annotation<A>{
	
	private final AnnotationIndexToken<A> annotation;
	private final TermOffsetIndexToken offset;
	private PositionIndexToken docPosition;
	
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
		if(annotation.getAnnotation() == null){
			try {
				throw new IOException("This Annotation object was passed a null object on instantiation. Check the instantiating Annotator configuration.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return annotation.getAnnotation();
	}
	
	public void setDocumentPosition(int pos){
		docPosition = new PositionIndexToken(pos);
	}
	
	//TODO: Create a better annotation hash code.
	public int annotationHashCode(){
		return annotation.hashCode();
	}
	
	public TermOffsetIndexToken getIndex() {
		return offset;
	}
	
	public PositionIndexToken getDocumentPosition() {
		return docPosition;
	}
	
	/**
	 * Used to ascertain if an annotation is defined by a specific annotation or a sub-section of a document or text.
	 * @return
	 */
	public boolean isOffsetAnnotation(){
		return annotation.getAnnotation() == null;
	}

}

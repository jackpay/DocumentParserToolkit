package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.IndexToken;
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

}

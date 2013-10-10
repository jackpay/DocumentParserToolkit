package ac.uk.susx.tag.annotation;

import java.util.HashMap;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 *
 */
public abstract class AbstractAnnotation<A> implements IAnnotation<A>{
	
	private final AnnotationIndexToken<A> annotation;
	private final TermOffsetIndexToken offset;
	private final HashMap<Class<? extends IIndexToken>, IIndexToken> index;
	private PositionIndexToken docPosition;
	
	public AbstractAnnotation(A annotation, int start, int end){
		this.annotation = (annotation == null)? null : new AnnotationIndexToken<A>(annotation);
		this.offset = new TermOffsetIndexToken(start,end);
		this.index = new HashMap<Class<? extends IIndexToken>, IIndexToken>();
		index.put(this.annotation.getClass(), this.annotation);
		index.put(this.offset.getClass(), this.offset);
	}
	
	public int getStart(){
		return offset.getStart();
	}
	
	public int getEnd(){
		return offset.getEnd();
	}
	
	public A getAnnotation(){
		return (annotation == null) ? null : annotation.getAnnotation();
	}
	
	public void setDocumentPosition(int pos){
		docPosition = new PositionIndexToken(pos);
	}
	
	public int annotationHashCode(){
		return annotation.hashCode();
	}
	
	public TermOffsetIndexToken getOffset() {
		return offset;
	}
	
	public PositionIndexToken getPosition() {
		return docPosition;
	}
	
	public IIndexToken getIndexToken(Class<? extends IIndexToken> indexClass) throws Exception {
		if(index.get(indexClass) == null){
			throw new Exception("The annotation: " + annotation.getAnnotation().toString() + "At Offset: " + getStart() + ":" + getEnd() + ". Does not have the required IndexToken.");
		}
		return index.get(indexClass);
	}

}

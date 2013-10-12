package ac.uk.susx.tag.annotation;

import java.util.HashMap;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.IIndexToken;
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
	
	public void addIndexToken(IIndexToken token) {
		index.put(token.getClass(), token);
	}
	
	public <IT extends IIndexToken> IT getIndexToken(Class<IT> indexClass) throws Exception {
		if(index.get(indexClass) != null){
			if(!index.get(indexClass).getClass().equals(indexClass)){
				throw new Exception("The class of the stored index token does not match the required class. Check how IIndexToken objects are added.");
			}
		}
		return indexClass.cast(index.get(indexClass));
	}

}

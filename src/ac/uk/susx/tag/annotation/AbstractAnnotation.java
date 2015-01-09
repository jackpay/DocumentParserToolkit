package ac.uk.susx.tag.annotation;

import java.util.HashMap;

import ac.uk.susx.tag.indexing.AnnotationIndexToken;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.OffsetIndexToken;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 */
public abstract class AbstractAnnotation<A> implements IAnnotation<A>{
	
	private AnnotationIndexToken<A> annotation;
	private final OffsetIndexToken offset;
	private final HashMap<Class<? extends IIndexToken>, IIndexToken> index;
	
	public AbstractAnnotation(A annotation, int start, int end){
		this.annotation = (annotation == null)? null : new AnnotationIndexToken<A>(annotation);
		this.offset = new OffsetIndexToken(start,end);
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
	
	public void setAnnotation(A annotation) {
		this.annotation = (annotation == null)? null : new AnnotationIndexToken<A>(annotation);
	}
	
	public A getAnnotation(){
		return (annotation == null) ? null : annotation.getAnnotation();
	}
	
	public OffsetIndexToken getOffset() {
		return offset;
	}
	
	public void addIndex(IIndexToken token) {
		index.put(token.getClass(), token);
	}
	
	public <IT extends IIndexToken> IT getIndex(Class<IT> indexClass) throws Exception {
		if(index.get(indexClass) != null){
			if(!index.get(indexClass).getClass().equals(indexClass)){
				throw new Exception("The class of the stored index token does not match the required class. Check how IIndexToken objects are added.");
			}
		}
		return indexClass.cast(index.get(indexClass));
	}
	
	@Override
	public final boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		AbstractAnnotation aa;
		if(obj instanceof AbstractAnnotation) {
			aa = (AbstractAnnotation) obj;
		}
		else{
			return false;
		}
		try {
			if(aa.getIndex(AnnotationIndexToken.class).equals(getIndex(AnnotationIndexToken.class))) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;	
	}

	@Override
	public final int hashCode() {
		int hash = 0;
		try {
			hash = getIndex(AnnotationIndexToken.class).hashCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	public String toString() {
		return formatForOutput();
	}
	
	public abstract String formatForOutput();

}

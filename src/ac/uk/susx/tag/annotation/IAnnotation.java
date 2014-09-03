package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.OffsetIndexToken;

public interface IAnnotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public void addIndex(IIndexToken token);
	
	public <IT extends IIndexToken> IT getIndex(Class<IT> indexClass) throws Exception;
	
	public OffsetIndexToken getOffset();
	
	public void setAnnotation(A annotation);
	
	public CharSequence formatForOutput();
	
}

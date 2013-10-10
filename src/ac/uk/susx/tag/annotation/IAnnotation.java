package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;

public interface IAnnotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public PositionIndexToken getPosition();
	
	public TermOffsetIndexToken getOffset();
	
	public void setDocumentPosition(int pos);
	
	public IIndexToken getIndexToken(Class<? extends IIndexToken> indexClass) throws Exception;
	
}

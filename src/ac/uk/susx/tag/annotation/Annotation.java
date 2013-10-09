package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;

public interface Annotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public PositionIndexToken getPosition();
	
	public TermOffsetIndexToken getOffset();
	
	public void setDocumentPosition(int pos);
	
	public IndexToken getIndex(Class<? extends IndexToken> indexClass);
	
}

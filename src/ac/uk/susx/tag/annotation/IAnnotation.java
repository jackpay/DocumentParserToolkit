package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.OffsetIndexToken;

public interface IAnnotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public void addIndexToken(IIndexToken token);
	
	public <IT extends IIndexToken> IT getIndexToken(Class<IT> indexClass) throws Exception;
	
	public OffsetIndexToken getOffsetIndex();
	
	public CharSequence formatForOutput();
	
}

package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IndexToken;

public interface Annotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public IndexToken getOffset();
	
	public void setDocumentPosition(int pos);
	
	public boolean isEmptyAnnotation();
	
}

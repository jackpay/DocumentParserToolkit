package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.indexing.IndexToken;

public interface Annotation<A>{
	
	public A getAnnotation();
	
	public int getStart();
	
	public int getEnd();
	
	public IndexToken getPositionIndex();
	
}

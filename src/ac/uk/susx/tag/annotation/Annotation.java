package ac.uk.susx.tag.annotation;

public interface Annotation<A>{
	
	public int hashCode();
	
	public A getAnnotation();
	
	public void setAnnotation(A annotation);
	
	public int getStart();
	
	public int getEnd();
	
}

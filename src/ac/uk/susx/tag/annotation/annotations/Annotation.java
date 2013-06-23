package ac.uk.susx.tag.annotation.annotations;

public interface Annotation<A>{
	
	public int hashCode();
	
	public A getAnnotation();
	
	public void setAnnotation(A annotation);
	
	public int getStart();
	
	public int getEnd();
	
}

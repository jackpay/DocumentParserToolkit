package ac.uk.susx.tag.annotation.annotations;

public interface Annotation<A,T>{
	
	public int hashCode();
	
	public T getToken();
	
	public A getAnnotation();

	public void setToken(T token);
	
	public void setAnnotation(A annotation);
	
}

package annotation;

public interface Annotation<A,T>{
	
	public int hashCode();
	
	public T getToken();
	
	public A getAnnotation();

	public void setToken();
	
	public void setAnnotation();
	
}

package annotation.annotations;

import java.util.Collection;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 *
 */
public abstract class AbstractAnnotation<A,T> implements Annotation<A,T>{
	
	protected A annotation;
	protected T token; 
	private int position;
	private int start;
	private int end;
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}
	
	public int getPosition(){
		return position;
	}
	
	public A getAnnotation(){
		return annotation;
	}
	
	public T getToken(){
		return token;
	}
	
	public void setToken(T token){
		this.token = token;
	}
	
	public void setAnnotation(A annotation){
		this.annotation = annotation;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
	
	public void setPosition(int pos){
		this.position = pos;
	}
	
	
	public int hashCode(){
		int prime = 31;
		int hash = prime * 17 + start;
		hash = hash * 31 + end;
		hash = hash * 13 + position;
		hash = hash * 23 + token.hashCode();
		return hash;
	}
	
	public A[] annotationsToArray(Collection<AbstractAnnotation<A,T>> annotations){
		AbstractAnnotation<A,T>[] anns = (AbstractAnnotation<A,T>[]) annotations.toArray();
		Object[] rawAnnotations = new Object[anns.length];
		for(int i = 0; i < anns.length; i++){
			rawAnnotations[i] = anns[i].getAnnotation();
		}
		return (A[]) rawAnnotations;
	}
}

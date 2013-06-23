package ac.uk.susx.tag.annotation.annotations;


/**
 * Abstract class for defining an annotation at a specific position within a document.
 * @author jackpay
 *
 */
public abstract class AbstractAnnotation<A> implements Annotation<A>{
	
	protected A annotation;
	private int docposition;
	private int start;
	private int end;
	
	public AbstractAnnotation(){}
	
	public AbstractAnnotation(A annotation, int start, int end){
		this.annotation = annotation;
		this.start = start;
		this.end = end;
	}
	
	public int getStart(){
		return start;
	}
	
	public int getEnd(){
		return end;
	}
	
	public int getPosition(){
		return docposition;
	}
	
	public A getAnnotation(){
		return annotation;
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
	
	public void setDocPosition(int pos){
		this.docposition = pos;
	}
	
	public int hashCode(){
		int prime = 31;
		int hash = prime * 13 + start;
		hash = hash * 23 + end;
		return hash;
	}
	
}

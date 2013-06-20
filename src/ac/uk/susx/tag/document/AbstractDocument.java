package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;

public class AbstractDocument <D,T> implements Document<D>{
	
	private D document;

	private Map<Class<? extends Annotator>, Collection<? extends Annotation>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
	}
	
	public D getDocument(){
		return document;
	}
	
	public Map<Class<? extends Annotator>,Collection<? extends Annotation>> getDocumentAnnotations() {
		return annotations;
	}
	
	public void addAnnotations(Class<? extends Annotator> cl, Collection<? extends Annotation> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, annotations);
		}
		else{
			//this.annotations.get(cl).addAll(annotations);
		}

	}

	public void setDocument(D docText) {
		this.document = docText;
	}

	public Collection<? extends Annotation> getAnnotations(Class<? extends Annotator> cl) {
		return annotations.get(cl);
	}

}

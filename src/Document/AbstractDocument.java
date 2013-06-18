package Document;

import java.util.Collection;
import java.util.Map;

import annotation.annotation.Annotation;
import annotation.annotator.Annotator;

public class AbstractDocument <D>{
	
	private D document;
	private Map<Class<Annotator>, Collection<Annotation>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
	}
	
	public D getDocument(){
		return document;
	}
	
	public Map<Class<Annotator>,Collection<Annotation>> getDocumentAnnotations() {
		return annotations;
	}
	
	public void addAnnotations(Class<Annotator> cl, Collection<Annotation> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, annotations);
		}
		else{
			this.annotations.get(cl).addAll(annotations);
		}

	}

}

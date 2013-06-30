package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;

public abstract class AbstractDocument <D,AT> implements Document<D,AT>{
	
	private D document;
	private Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
		annotations = new HashMap<Class<? extends Annotator>, Collection<Annotation<AT>>>(10);
	}
	
	public D getDocument(){
		return document;
	}
	
	public void setDocument(D docText) {
		this.document = docText;
	}


	@SuppressWarnings("rawtypes")
	public Collection<? extends Annotation<AT>> getAnnotations(
			Class<? extends Annotator> cl) {
		return annotations.get(cl);
	}

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends Annotator>, Collection<Annotation<AT>>> getDocumentAnnotations() {
		return annotations;
	}

	@SuppressWarnings("rawtypes")
	public void addAnnotations(
			Class<? extends Annotator> cl,
			Collection<Annotation<AT>> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, new ArrayList<Annotation<AT>>());
			this.annotations.get(cl).addAll(annotations);
		}
		else{
			this.annotations.get(cl).addAll(annotations);
		}
	}

	public void removeAnnotation(Class<? extends Annotator> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}

	@SuppressWarnings("rawtypes")
	public void removeAnnotations(Collection<Class<? extends Annotator>> annotators) {
		for(Class<? extends Annotator> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	public void retainAnnotations(
			Collection<Class<? extends Annotator>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
}

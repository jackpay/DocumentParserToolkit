package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.filter.Filter;
import ac.uk.susx.tag.indexing.IndexToken;

public abstract class AbstractDocument <D,AT> implements Document<D,AT>{
	
	private D document;
	private Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
		annotations = new HashMap<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>>(10);
	}
	
	public D getDocument(){
		return document;
	}
	
	public void setDocument(D docText) {
		this.document = docText;
	}


	@SuppressWarnings("rawtypes")
	public Map<IndexToken, Annotation<AT>> getAnnotations(
			Class<? extends Annotator> cl) {
		return annotations.get(cl);
	}

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> getDocumentAnnotations() {
		return annotations;
	}

	@SuppressWarnings("rawtypes")
	public void addAnnotations(Class<? extends Annotator> cl, Map<IndexToken, Annotation<AT>> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, new HashMap<IndexToken,Annotation<AT>>());
			this.annotations.get(cl).putAll(annotations);
		}
		else{
			this.annotations.get(cl).putAll(annotations);
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
	
	public void filterAnnotations(Collection<Filter<AT>> filters){
		if(filters != null && !filters.isEmpty()){
			for(Filter<AT> filter : filters){
				filter.filterCollection(annotations);
			}
		}
	}
	
	public void filterAnnotation(Collection<Filter<AT>> filters, Class<? extends Annotator> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(Filter<AT> filter : filters){
				filter.filter(annotations.get(annotator));
			}
		}
	}

}

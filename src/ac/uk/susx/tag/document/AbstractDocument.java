package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.indexing.IIndexToken;

public abstract class AbstractDocument <D,AT> implements IDocument<D,AT>{
	
	private D document;
	private Map<Class<? extends IAnnotator>, List<IAnnotation<AT>>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
		annotations = new HashMap<Class<? extends IAnnotator>, List<IAnnotation<AT>>>(10);
	}
	
	public D getDocument(){
		return document;
	}
	
	public void setDocument(D docText) {
		this.document = docText;
	}


	@SuppressWarnings("rawtypes")
	public List<IAnnotation<AT>> getAnnotations(
			Class<? extends IAnnotator> cl) {
		return annotations.get(cl);
	}

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends IAnnotator>, List<IAnnotation<AT>>> getDocumentAnnotations() {
		return annotations;
	}

	@SuppressWarnings("rawtypes")
	public void addAnnotations(
			Class<? extends IAnnotator> cl,
			List<IAnnotation<AT>> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, new ArrayList<IAnnotation<AT>>());
			this.annotations.get(cl).addAll(annotations);
		}
		else{
			this.annotations.get(cl).addAll(annotations);
		}
	}

	public void removeAnnotation(Class<? extends IAnnotator> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}

	@SuppressWarnings("rawtypes")
	public void removeAnnotations(List<Class<? extends IAnnotator>> annotators) {
		for(Class<? extends IAnnotator> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	public void retainAnnotations(
			Collection<Class<? extends IAnnotator>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
	
	public void filterAnnotations(Collection<IFilter<AT>> filters){
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filterCollection(annotations);
			}
		}
	}
	
	public void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filter(annotations.get(annotator));
			}
		}
	}

}

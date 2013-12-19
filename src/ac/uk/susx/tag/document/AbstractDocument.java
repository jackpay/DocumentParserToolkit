package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.indexing.IIndexToken;

public abstract class AbstractDocument <D> implements IDocument<D>{
	
	private D document;
	private Map<Class<? extends IAnnotator<IDocument<D>,?,?>>, List<? extends IAnnotation<?>>> annotations;
	
	public AbstractDocument(D rawDoc){
		this.document = rawDoc;
		annotations = new HashMap<Class<? extends IAnnotator<IDocument<D>,?,?>>, List<? extends IAnnotation<?>>>(10);
	}
	
	public D getDocument(){
		return document;
	}
	
	public void setDocument(D docText) {
		this.document = docText;
	}

	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator<IDocument<D>, IAnnotation<AT>,?>> cl) {
		return (List<IAnnotation<AT>>) annotations.get(cl);
	}

	public Collection<List<? extends IAnnotation<?>>> getDocumentAnnotations() {
		return annotations.values();
	}

	public <AT> void addAnnotations(Class<? extends IAnnotator<IDocument<D>, IAnnotation<AT>,?>> cl, List<? extends IAnnotation<AT>> annotations) {
		List<IAnnotation<AT>> anns = (List<IAnnotation<AT>>) this.annotations.get(cl);
		if(anns == null){
			this.annotations.put(cl, new ArrayList<IAnnotation<AT>>());
			anns = (List<IAnnotation<AT>>) this.annotations.get(cl);
			anns.addAll(annotations);
		}
		else{
			anns.addAll(annotations);
		}
	}

	public void removeAnnotation(Class<? extends IAnnotator<IDocument<D>,?,?>> cl) {
		if(annotations.containsKey(cl)){
			annotations.remove(cl);
		}
	}

	public void removeAnnotations(Collection<Class<? extends IAnnotator<IDocument<D>,?,?>>> annotators) {
		for(Class<? extends IAnnotator<IDocument<D>,?,?>> annotator : annotators){
			removeAnnotation(annotator);
		}
	}

	public void retainAnnotations(Collection<Class<? extends IAnnotator<IDocument<D>,?,?>>> includedAnnotators) {
		annotations.keySet().retainAll(includedAnnotators);
	}
	
	public <AT> void filterAnnotations(Collection<IFilter<AT>> filters){
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				filter.filterCollection(Maps.filterEntries(annotations, new ClassSpecificPredicate(IAnnotation<AT>.class)));
			}
		}
	}
	
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<?,IAnnotation<AT>,?>> annotator) {
		if(filters != null && !filters.isEmpty()){
			for(IFilter<AT> filter : filters){
				List<IAnnotation<AT>> anns = (List<IAnnotation<AT>>) annotations.get(annotator);
				filter.filter(anns);
			}
		}
	}
	
	private static class ClassSpecificPredicate<AT> implements Predicate<Map.Entry<Class<? extends IAnnotator>, List<? extends IAnnotation<?>>>> {
		
		Class<AT> compClass;
		
		private ClassSpecificPredicate(Class<AT> cl) {
			this.compClass = cl;
		}

		public boolean apply(Entry<Class<? extends IAnnotator>, List<? extends IAnnotation<?>>> arg0) {
			arg0.getValue().getClass().getTypeParameters().equals(compClass); 
			return arg0.getValue().getClass().getTypeParameters()[0].getClass().getTypeParameters()[0].equals(compClass);
		}
		
	}

}

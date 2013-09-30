package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.filter.Filter;
import ac.uk.susx.tag.indexing.IndexToken;

public interface Document <D,AT>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	@SuppressWarnings("rawtypes")
	public void addAnnotations(Class<? extends Annotator> cl, Map<IndexToken, Annotation<AT>> annotations);
	
	@SuppressWarnings("rawtypes")
	public Map<IndexToken, Annotation<AT>> getAnnotations(Class<? extends Annotator> cl);
	
	@SuppressWarnings("rawtypes")
	public Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> getDocumentAnnotations();
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotations(Collection<Class<? extends Annotator>> excludedAnnotators);
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotation(Class<? extends Annotator> cl);
	
	@SuppressWarnings("rawtypes")
	public void retainAnnotations(Collection<Class<? extends Annotator>> includedAnnotators);
	
	public void filterAnnotations(Collection<Filter<AT>> filters);
	
	public void filterAnnotation(Collection<Filter<AT>> filters, Class<? extends Annotator> annotator);
	
}

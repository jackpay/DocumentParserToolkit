package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;

public interface Document <D,AT>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	@SuppressWarnings("rawtypes")
	public void addAnnotations(Class<? extends Annotator> cl, Collection<Annotation<AT>> annotations);
	
	@SuppressWarnings("rawtypes")
	public Collection<? extends Annotation<AT>> getAnnotations(Class<? extends Annotator> cl);
	
	@SuppressWarnings("rawtypes")
	public Map<Class<? extends Annotator>, Collection<Annotation<AT>>> getDocumentAnnotations();
	
}

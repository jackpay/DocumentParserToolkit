package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;

public interface Document <D>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	public void addAnnotations(Class<? extends Annotator> cl, Collection<? extends Annotation> annotations);
	
	public Collection<? extends Annotation> getAnnotations(Class<? extends Annotator> cl);
	
	public Map<Class<? extends Annotator>, Collection<? extends Annotation>> getDocumentAnnotations();
}

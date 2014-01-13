package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;

public interface IDocument <D>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	public <AT> void addAnnotations(Class<? extends IAnnotator<AT, D,?>> cl, List<? extends IAnnotation<AT>> annotations);
	
	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator<AT,?,?>> cl);
	
	public Collection<List<? extends IAnnotation<?>>> getDocumentAnnotations();
	
	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> excludedAnnotators);
	
	public void removeAnnotation(Class<? extends IAnnotator<?,?,?>> cl);
	
	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> includedAnnotators);
	
	public void filterAnnotations(Collection<IFilter<?>> filters);
	
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?,?>> annotator);
	
}

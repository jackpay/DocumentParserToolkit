package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;

public interface IDocument <D>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	@SuppressWarnings("rawtypes")
	public <AT> void addAnnotations(Class<? extends IAnnotator> cl, List<IAnnotation<AT>> annotations);
	
	@SuppressWarnings("rawtypes")
	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator> cl);
	
	@SuppressWarnings("rawtypes")
	public Map<Class<? extends IAnnotator>, List<IAnnotation<?>>> getDocumentAnnotations();
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotations(List<Class<? extends IAnnotator>> excludedAnnotators);
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotation(Class<? extends IAnnotator> cl);
	
	@SuppressWarnings("rawtypes")
	public void retainAnnotations(Collection<Class<? extends IAnnotator>> includedAnnotators);
	
	public <AT >void filterAnnotations(Collection<IFilter<AT>> filters);
	
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator> annotator);
	
}

package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;

public interface IDocument <D,AT>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	@SuppressWarnings("rawtypes")
	public void addAnnotations(Class<? extends IAnnotator> cl, List<IAnnotation<AT>> annotations);
	
	@SuppressWarnings("rawtypes")
	public List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator> cl);
	
	@SuppressWarnings("rawtypes")
	public Map<Class<? extends IAnnotator>, List<IAnnotation<AT>>> getDocumentAnnotations();
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotations(List<Class<? extends IAnnotator>> excludedAnnotators);
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotation(Class<? extends IAnnotator> cl);
	
	@SuppressWarnings("rawtypes")
	public void retainAnnotations(Collection<Class<? extends IAnnotator>> includedAnnotators);
	
	public void filterAnnotations(Collection<IFilter<AT>> filters);
	
	public void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator> annotator);
	
}

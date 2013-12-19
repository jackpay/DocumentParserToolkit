package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;

public interface IDocument <D>{
	
	public D getDocument();
	
	public void setDocument(D docText);

	public <AT> void addAnnotations(Class<? extends IAnnotator<IDocument<D>, IAnnotation<AT>,?>> cl, List<? extends IAnnotation<AT>> annotations);
	
	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator<IDocument<D>,IAnnotation<AT>,?>> cl);
	
	public Collection<List<? extends IAnnotation<?>>> getDocumentAnnotations();
	
	public void removeAnnotations(Collection<Class<? extends IAnnotator<IDocument<D>,?,?>>> excludedAnnotators);
	
	public void removeAnnotation(Class<? extends IAnnotator<IDocument<D>,?,?>> cl);
	
	public void retainAnnotations(Collection<Class<? extends IAnnotator<IDocument<D>,?,?>>> includedAnnotators);
	
	public <AT> void filterAnnotations(Collection<IFilter<AT>> filters);
	
	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<?,IAnnotation<AT>,?>> annotator);
	
}

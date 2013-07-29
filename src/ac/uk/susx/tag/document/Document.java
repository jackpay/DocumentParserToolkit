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
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotations(Collection<Class<? extends Annotator>> excludedAnnotators);
	
	@SuppressWarnings("rawtypes")
	public void removeAnnotation(Class<? extends Annotator> cl);
	
	@SuppressWarnings("rawtypes")
	public void retainAnnotations(Collection<Class<? extends Annotator>> includedAnnotators);
	
	/**
	 * Used to get a sub-section of the stored document. Primarily intended for use by the Annotation offsets.
	 * @param start 
	 */
	public D getDocumentSubSection(int start);
	
	/**
	 * Used to get a sub-section of the stored document. Primarily intended for use by the Annotation offsets.
	 * @param start 
	 * @param end
	 */
	public D getDocumentSubSection(int start, int end);
	
}

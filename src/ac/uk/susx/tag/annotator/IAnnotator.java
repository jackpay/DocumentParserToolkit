package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface IAnnotator <D extends IDocument<?>, A extends IAnnotation<?>> {
	
	public void annotate(D document) throws IncompatibleAnnotationException;
	
	public void annotate(D doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public <AT> List<A> annotate(List<? extends IAnnotation<AT>> annotations) throws IncompatibleAnnotationException;
	
	public <AT> List<A> annotate(IAnnotation<AT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

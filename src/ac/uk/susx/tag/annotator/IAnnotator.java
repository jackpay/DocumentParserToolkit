package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface IAnnotator <D extends IDocument<DT,AT>, A extends IAnnotation<AT>,AT,DT> {
	
	public void annotate(D document) throws IncompatibleAnnotationException;
	
	public void annotate(D doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public List<A> annotate(List<? extends IAnnotation<AT>> annotations) throws IncompatibleAnnotationException;
	
	public List<A> annotate(IAnnotation<AT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

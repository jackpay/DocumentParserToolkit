package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface IAnnotator <AT,DT,ACT> {
	
	public void annotate(IDocument<DT> document) throws IncompatibleAnnotationException;
	
	public void annotate(IDocument<DT> doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public List<IAnnotation<AT>> annotate(List<? extends IAnnotation<ACT>> annotations) throws IncompatibleAnnotationException;
	
	public List<IAnnotation<AT>> annotate(IAnnotation<ACT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

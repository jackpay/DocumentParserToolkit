package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * The interface class describing the basic methods required for an annotator.
 * @author jp242
 *
 * @param <AT> The output annotation type
 * @param <DT> The document type
 * @param <ACT> The accepted input annotation type
 */
public interface IAnnotator <AT,DT,ACT> {
	
	/**
	 * Annotate the text of an entire document object
	 * @param document The document to be annotated
	 * @throws IncompatibleAnnotationException 
	 */
	public IDocument<DT> annotate(IDocument<DT> document) throws IncompatibleAnnotationException;
	
	public SentenceAnnotation annotate(SentenceAnnotation sentence) throws IncompatibleAnnotationException;
	
	public List<IAnnotation<AT>> annotate(List<? extends IAnnotation<ACT>> annotations) throws IncompatibleAnnotationException;
	
	public List<IAnnotation<AT>> annotate(IAnnotation<ACT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

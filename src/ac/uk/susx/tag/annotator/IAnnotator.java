package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.SentenceAnnotation;
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
	 * @param doc The document to be annotated
	 * @throws IncompatibleAnnotationException 
	 */
	public IDocument<DT> annotate(IDocument<DT> doc) throws IncompatibleAnnotationException;
	
	/**
	 * Annotate a list of exisiting annotations.
	 * @param annotations The list of annotations to annotate
	 * @return A list of newly created annotations corresponding to the annotated input list.
	 * @throws IncompatibleAnnotationException
	 */
	public List<? extends IAnnotation<AT>> annotate(List<? extends IAnnotation<ACT>> annotations) throws IncompatibleAnnotationException;
	
	/**
	 * Annotate a single SentenceAnnotation object
	 * @param sentence The sentence to annotate
	 * @return The list of newly created annotations
	 * @throws IncompatibleAnnotationException
	 */
	public List<? extends IAnnotation<AT>> annotate(SentenceAnnotation<ACT> sentence) throws IncompatibleAnnotationException;
	
	/**
	 * Annotate a single annotation
	 * @param annotation The Annotation
	 * @return A list of Annotations born
	 * @throws IncompatibleAnnotationException
	 */
	public List<? extends IAnnotation<AT>> annotate(IAnnotation<ACT> annotation) throws IncompatibleAnnotationException;
	

	/**
	 * Start the annotator model
	 */
	public void startModel();
	
	/**
	 * Check if the Annotator model is already running
	 * @return True if the model  is already running
	 */
	public boolean modelStarted();
	
}

package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * The interface class describing the basic methods required for an annotator.
 * @author jp242
 *
 * @param <AT> The output annotation type
 * @param <DT> The document type
 * @param <ACT> The accepted input annotation type
 */
public interface IAnnotator <AT,ACT> {
	
	/**
	 * Annotate the text of an entire document object
	 * @param doc The document to be annotated
	 * @throws IncompatibleAnnotationException 
	 */
	public Document annotate(Document doc) throws IncompatibleAnnotationException;
	
	/**
	 * Annotate a single SentenceAnnotation object
	 * @param sentence The sentence to annotate
	 * @return The list of newly created annotations
	 * @throws IncompatibleAnnotationException
	 */
	public List<? extends Annotation<AT>> annotate(Sentence sentence) throws IncompatibleAnnotationException;
	
	/**
	 * Annotate a single annotation.
	 * @param annotation The Annotation
	 * @return A list of Annotations born
	 * @throws IncompatibleAnnotationException
	 */
	public List<? extends Annotation<AT>> annotate(Annotation<ACT> annotation) throws IncompatibleAnnotationException;
	

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

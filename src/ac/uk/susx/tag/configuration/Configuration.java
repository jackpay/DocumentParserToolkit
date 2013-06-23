package ac.uk.susx.tag.configuration;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

/**
 * The interace class for a global config file.
 * @author jackpay
 *
 */
public interface Configuration <D extends Document<?,AT>, A extends Annotation<AT>, AT >{	
	
	/**
	 * Add an annotator to the collection of annotators to use.
	 * @param annotator
	 */
	public void addAnnotator(Annotator<D,A,AT> annotator);
	
	/**
	 * Add an annotator to the collection of annotators and specify if its annotations will be included in the output.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(Annotator<D,A,AT> annotator, boolean include);
	
	/**
	 * @return Return all stored annotators.
	 */
	public Collection<Annotator<D,A,AT>> getAnnotators();
	
	/**
	 * @return Return all annotators which will have all their annotations included in the output.
	 */
	public Collection<Annotator<D,A,AT>> getOutputIncludedAnnotators();
	
	/**
	 * @return Return the output location.
	 */
	public String getInputLocation();
	
	/**
	 * @return Return the input location.
	 */
	public String getOutputLocation();

}
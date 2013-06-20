package ac.uk.susx.tag.configuration;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotator.Annotator;

/**
 * The interace class for a global config file.
 * @author jackpay
 *
 */
public interface Configuration <T,A>{	
	
	/**
	 * Add an annotator to the collection of annotators to use.
	 * @param annotator
	 */
	public void addAnnotator(Annotator annotator);
	
	/**
	 * Add an annotator to the collection of annotators and specify if its annotations will be included in the output.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(Annotator annotator, boolean include);
	
	/**
	 * @return Return all stored annotators.
	 */
	public Collection<Annotator> getAnnotators();
	
	/**
	 * @return Return all annotators which will have all their annotations included in the output.
	 */
	public Collection<Annotator> getOutputIncludedAnnotators();
	
	/**
	 * @return Return the output location.
	 */
	public T getInputLocation();
	
	/**
	 * @return Return the input location.
	 */
	public A getOutputLocation();
	
	/**
	 * Set the input location.
	 * @param inputLoc
	 */
	public void setInputLocation(T inputLoc);
	
	/**
	 * Set the output location.
	 * @param outputLoc
	 */
	public void setOutputLocation(A outputLoc);
}

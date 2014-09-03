package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.input.CommandLineOption;
import ac.uk.susx.tag.utils.IllegalInputParamsException;

/**
 * To use an on object of this class the class must be given the @AnnotatorFactory annotation for class registration to occur and have no 
 * input parameters to the constructor.
 * @author jp242
 *
 * @param <AT>
 * @param <DT>
 * @param <ACT>
 */
public interface IAnnotatorFactory <AT,ACT>{
	
	/**
	 * Creates the IAnnotator object.
	 * @return
	 */
	public abstract IAnnotator<AT,ACT> create();
	
	/**
	 * @param params Any additional paramaters required to instantiate the object.
	 * @return The new instantiated annotator object.
	 * @throws IllegalInputParamsException if it cannot parse the input params
	 */
	public abstract IAnnotator<AT,ACT> create(String[] params) throws IllegalInputParamsException;
	
	/**
	 * Retrieves the command line option for the associated IAnnotator.
	 * @return
	 */
	public abstract CommandLineOption getCommandLineOption();
	
}
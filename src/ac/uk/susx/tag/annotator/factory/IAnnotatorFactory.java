package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.input.CommandLineOption;

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
	 * Retrieves the command line option for the associated IAnnotator.
	 * @return
	 */
	public abstract CommandLineOption getCommandLineOption();
	
//	/**
//	 * Use this to pass command line parameters to your annotator instance.
//	 * @param params
//	 */
//	public abstract void parseInputParams(Object... params);
	
}
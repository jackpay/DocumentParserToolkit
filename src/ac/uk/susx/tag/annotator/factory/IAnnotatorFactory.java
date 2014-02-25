package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.indexing.AnnotatorIndexToken;

/**
 * To use an on object of this class the class must be given the @AnnotatorFactory annotation for class registration to occur and have no 
 * input parameters to the constructor.
 * @author jp242
 *
 * @param <AT>
 * @param <DT>
 * @param <ACT>
 */
public interface IAnnotatorFactory <AT,DT,ACT>{
	
	/**
	 * Creates the IAnnotator object.
	 * @return
	 */
	public abstract IAnnotator<AT,DT,ACT> create();
	
	/**
	 * Retrieves the command line option for the associated IAnnotator.
	 * @return
	 */
	public abstract String getCommandLineOption();
	
}
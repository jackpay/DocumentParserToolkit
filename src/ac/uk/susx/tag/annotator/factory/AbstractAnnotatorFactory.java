package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.indexing.AnnotatorIndexToken;

public abstract class AbstractAnnotatorFactory <AT,DT,ACT>{
	
	private AnnotatorIndexToken index;
	
	{
		index = AnnotatorIndexToken.generateIndexToken(this);
		AnnotatorRegistry.register(this);
	}
	
	protected abstract IAnnotator<AT,DT,ACT> create();

	public abstract String getFactoryId();
	
	public abstract String getCommandLineOption();
	
}
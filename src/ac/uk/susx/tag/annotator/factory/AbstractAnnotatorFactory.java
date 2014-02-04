package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;

public abstract class AbstractAnnotatorFactory {
	
	private final String cmnd;
	private final String ID;
	
	{
		AnnotatorRegistry.register(this);
	}
	
	protected abstract IAnnotator<?,?,?> create();
	
	protected abstract String getID();

}
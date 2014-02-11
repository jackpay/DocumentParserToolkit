package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.indexing.AnnotatorIndexToken;

public abstract class AbstractAnnotatorFactory {
	
	private AnnotatorIndexToken index;
	
	{
		index = AnnotatorIndexToken.generateIndexToken(this);
		AnnotatorRegistry.register(index);
	}
	
	protected abstract IAnnotator<?,?,?> create();

	public abstract String getFactoryId();
	
	public abstract String getCommandLineOption();
	
}
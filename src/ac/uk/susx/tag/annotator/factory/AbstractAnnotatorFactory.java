package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;

public abstract class AbstractAnnotatorFactory {
	
	public AbstractAnnotatorFactory(String id) {
		AnnotatorRegistry.register(id, create());
	}
	
	public abstract <AT,DT,ACT> IAnnotator<AT,DT,ACT> create();
	
}
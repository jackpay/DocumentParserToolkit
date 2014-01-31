package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;

public abstract class AbstractAnnotatorFactory {
	
	public static final String ID;
	
	static {
		AnnotatorRegistry.register(ID);
	}
	
	public abstract String id();
	
	public abstract <AT,DT,ACT> IAnnotator<AT,DT,ACT> create();
	
}
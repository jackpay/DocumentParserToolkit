package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;

public abstract class AbstractAnnotatorFactory {
	
	protected abstract IAnnotator<?,?,?> create();
	
	public AbstractAnnotatorFactory(){}
	
	public static void registerNewAnnotator(Class<? extends AbstractAnnotatorFactory> factory) {
		try {
			AnnotatorRegistry.register(factory.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
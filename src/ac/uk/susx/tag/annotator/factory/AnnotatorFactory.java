package ac.uk.susx.tag.annotator.factory;

import java.util.HashMap;

import ac.uk.susx.tag.annotator.IAnnotator;

public class AnnotatorFactory {
	
	private final HashMap<Class<? extends IAnnotator>, IAnnotator> annotators;
	
	public AnnotatorFactory(){
		annotators = new HashMap<Class<? extends IAnnotator>, IAnnotator>();
	}

}

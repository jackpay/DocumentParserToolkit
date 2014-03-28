package ac.uk.susx.tag.filter;

import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class LowercaseFilter implements IFilter<String> {
	
	private final Class<? extends IAnnotator> annotator;
	
	public LowercaseFilter(Class<? extends IAnnotator> annotator){
		this.annotator = annotator;
	}

	public List<IAnnotation<String>> filter(List<IAnnotation<String>> annotations) {
		for(IAnnotation<String> anno : annotations) {
			annotations.set(annotations.indexOf(anno), new StringAnnotation(anno.getAnnotation().toLowerCase(),anno.getStart(),anno.getEnd()));
		}
		return annotations;
	}

	public Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> filterCollection(Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> annotations) {
		if(annotations.get(annotator) == null){
			return annotations;
		}
		annotations.put(annotator, filter(annotations.get(annotator)));
		return annotations;
	}

}

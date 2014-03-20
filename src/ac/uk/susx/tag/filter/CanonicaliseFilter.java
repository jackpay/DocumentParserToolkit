package ac.uk.susx.tag.filter;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class CanonicaliseFilter implements IFilter<String> {
	
	private final String canon;
	private final Pattern patt;
	
	public CanonicaliseFilter(String canon, Pattern patt, Class<? extends IAnnotator> annotator) {
		this.canon = canon;
		this.patt = patt;
	}

	public List<IAnnotation<String>> filter(List<IAnnotation<String>> annotations) {
		for(IAnnotation<String> ann : annotations) {
			if(matchAnnotation(ann.getAnnotation())) {
				annotations.set(annotations.indexOf(ann), new StringAnnotation(canon,ann.getStart(),ann.getEnd()));
			}
		}
		return annotations;
	}

	public Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> filterCollection(
			Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> annotations) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean matchAnnotation(String annotation) {
		Matcher m = patt.matcher(annotation);
		if(m.matches()){
			return true;
		}
		return false;
	}

}

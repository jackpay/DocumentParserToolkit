package ac.uk.susx.tag.filter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class RegexFilter extends AbstractAnnotationFilter<String>{
	
	public RegexFilter(List<String> filterAnnotations,Class<? extends IAnnotator> annotator) {
		super(filterAnnotations, annotator, true,true);
	}
	
	public RegexFilter(String pattern, Class<? extends IAnnotator> annotator) {
		super(pattern, annotator,true,true);
	}

	public boolean matchAnnotation(IAnnotation<String> annotation){
		for(String fAnn : getFilterAnnotations()){
			Pattern patt = Pattern.compile(fAnn);
			Matcher matcher = patt.matcher(annotation.getAnnotation());
			if(matcher.matches()){
				return true;
			}
		}
		return false;
	}

}

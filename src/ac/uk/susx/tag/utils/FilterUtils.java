package ac.uk.susx.tag.utils;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.Annotation;

public class FilterUtils {
	
	public static boolean annotationsIntersect(Annotation<?> ann1, Annotation<?> ann2) {
		if(ann1.getStart() == ann2.getStart() || ann1.getEnd() == ann2.getEnd() || 
				ann1.getStart() == ann2.getEnd() || ann1.getEnd() == ann2.getStart()){
			return true;
		}
		if(ann1.getStart() > ann2.getStart()){
			return ann2.getEnd() > ann1.getStart();
		}
		else{
			return ann1.getEnd() > ann2.getStart();
		}
	}
	
	public static boolean AnnotationsEqual(Annotation<?> ann1, Annotation<?> ann2) {
		return ann1.getAnnotation().equals(ann2.getAnnotation());
	}
	
	public static boolean regexMatchAnnotation(Annotation<String> ann, String patt) {
		return regexMatchAnnotation(ann,Pattern.compile(patt));
	}
	
	public static boolean regexMatchAnnotation(Annotation<String> ann, Pattern patt) {
		Matcher matcher = patt.matcher(ann.getAnnotation());
		return matcher.matches();
	}
	
	public class AnnotationPositionComparator implements Comparator<Annotation<?>> {

		public int compare(Annotation<?> ann1, Annotation<?> ann2) {
			return ann1.getPosition().getPosition() < ann2.getPosition().getPosition() ? -1 : ann1.getPosition().getPosition() == ann2.getPosition().getPosition() ? 0 : 1;
		}	
	}
	
	public class AnnotationOffsetComparator implements Comparator<Annotation<?>> {

		public int compare(Annotation<?> ann1, Annotation<?> ann2) {
			return ann1.getStart() < ann2.getStart() ? -1 : ann1.getStart() == ann2.getStart() ? 0 : 1;
		}
	}
}

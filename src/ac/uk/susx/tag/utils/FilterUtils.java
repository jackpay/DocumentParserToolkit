package ac.uk.susx.tag.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;

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
	
	public static <AT> Map<IndexToken, Annotation<AT>> annotationsToMap(Collection<Annotation<AT>> annotations){
		return annotationsToMap(annotations, TermOffsetIndexToken.class);
	}
	
	public static <AT> Map<IndexToken, Annotation<AT>> annotationsToMap(Collection<Annotation<AT>> annotations, Class<? extends IndexToken> index){
		Map<IndexToken, Annotation<AT>> annoMap = new HashMap<IndexToken, Annotation<AT>>();
		Iterator<Annotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			Annotation<AT> next = iter.next();
			annoMap.put(next.getIndex(index), next);
		}
		return annoMap;
	}
	
	public static <AT> Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotationsToMap(Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations){
		return annotationsToMap(annotations, TermOffsetIndexToken.class);
	}
	
	public static <AT> Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotationsToMap(Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations, Class<? extends IndexToken> index){
		Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annoMap = new HashMap<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>>();
		for(Class<? extends Annotator> annotator : annotations.keySet()){
			annoMap.put(annotator, annotationsToMap(annotations.get(annotator),index));
		}
		return annoMap;
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

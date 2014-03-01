package ac.uk.susx.tag.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.indexing.OffsetIndexToken;

public class FilterUtils {
	
	public static boolean annotationsIntersect(IAnnotation<?> ann1, IAnnotation<?> ann2) {
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
	
	public static boolean AnnotationsEqual(IAnnotation<?> ann1, IAnnotation<?> ann2) {
		return ann1.getAnnotation().equals(ann2.getAnnotation());
	}
	
	public static boolean regexMatchAnnotation(IAnnotation<String> ann, String patt) {
		return regexMatchAnnotation(ann,Pattern.compile(patt));
	}
	
	public static boolean regexMatchAnnotation(IAnnotation<String> ann, Pattern patt) {
		Matcher matcher = patt.matcher(ann.getAnnotation());
		return matcher.matches();
	}
	
	public static Map<IIndexToken, IAnnotation<?>> annotationsToMap(List<? extends IAnnotation<?>> annotations){
		return annotationsToMap(annotations, OffsetIndexToken.class);
	}
	
	public static Map<IIndexToken, IAnnotation<?>> annotationsToMap(List<? extends IAnnotation<?>> annotations, Class<? extends IIndexToken> index){
		Map<IIndexToken, IAnnotation<?>> annoMap = new HashMap<IIndexToken, IAnnotation<?>>();
		Iterator<? extends IAnnotation<?>> iter = annotations.iterator();
		while(iter.hasNext()){
			IAnnotation<?> next = iter.next();
			try {
				annoMap.put(next.getIndexToken(index), next);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return annoMap;
	}
	
	public static Map<Class<? extends IAnnotator<?,?>>, Map<IIndexToken, IAnnotation<?>>> annotationsToMap(Map<Class<? extends IAnnotator<?,?>>, List<? extends IAnnotation<?>>> annotations){
		return annotationsToMap(annotations, OffsetIndexToken.class);
	}
	
	public static Map<Class<? extends IAnnotator<?,?>>, Map<IIndexToken, IAnnotation<?>>> annotationsToMap(Map<Class<? extends IAnnotator<?,?>>, List<? extends IAnnotation<?>>> annotations, Class<? extends IIndexToken> index){
		Map<Class<? extends IAnnotator<?,?>>, Map<IIndexToken, IAnnotation<?>>> annoMap = new HashMap<Class<? extends IAnnotator<?,?>>, Map<IIndexToken, IAnnotation<?>>>();
		for(Class<? extends IAnnotator<?,?>> annotator : annotations.keySet()){
			annoMap.put(annotator, annotationsToMap(annotations.get(annotator),index));
		}
		return annoMap;
	}
	
	public static class AnnotationPositionComparator implements Comparator<IAnnotation<?>> {
		public int compare(IAnnotation<?> ann1, IAnnotation<?> ann2) {
			try {
				return ann1.getIndexToken(PositionIndexToken.class).getPosition() < ann2.getIndexToken(PositionIndexToken.class).getPosition() ? -1 : ann1.getIndexToken(PositionIndexToken.class).getPosition() == ann2.getIndexToken(PositionIndexToken.class).getPosition() ? 0 : 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}	
	}
	
	public static class AnnotationOffsetComparator implements Comparator<IAnnotation<?>> {
		public int compare(IAnnotation<?> ann1, IAnnotation<?> ann2) {
			return ann1.getStart() < ann2.getStart() ? -1 : ann1.getStart() == ann2.getStart() ? 0 : 1;
		}
	}
	
	
	
}

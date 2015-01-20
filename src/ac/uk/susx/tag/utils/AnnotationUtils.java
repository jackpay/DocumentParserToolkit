package ac.uk.susx.tag.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.indexing.OffsetIndexToken;
import ac.uk.susx.tag.indexing.PositionIndexToken;

public class AnnotationUtils {
	
	public static <A> A[] annotationsToArray(List<? extends Annotation<A>> annotations, A[] array){
		Iterator<? extends Annotation<A>> iter = annotations.iterator();
		int i = 0;
		while(iter.hasNext()){
			array[i] = (A) iter.next().getAnnotation();
			i++;
		}
		return (A[]) array;
	}
	
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
	
	public static Map<IndexToken, Annotation<?>> annotationsToMap(List<? extends Annotation<?>> annotations){
		return annotationsToMap(annotations, OffsetIndexToken.class);
	}
	
	public static Map<IndexToken, Annotation<?>> annotationsToMap(List<? extends Annotation<?>> annotations, Class<? extends IndexToken> index){
		Map<IndexToken, Annotation<?>> annoMap = new HashMap<IndexToken, Annotation<?>>();
		Iterator<? extends Annotation<?>> iter = annotations.iterator();
		while(iter.hasNext()){
			Annotation<?> next = iter.next();
			try {
				annoMap.put(next.getIndex(index), next);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return annoMap;
	}
	
	public static Map<Class<? extends IAnnotator<?,?>>, Map<IndexToken, Annotation<?>>> annotationsToMap(Map<Class<? extends IAnnotator<?,?>>, List<? extends Annotation<?>>> annotations){
		return annotationsToMap(annotations, OffsetIndexToken.class);
	}
	
	public static Map<Class<? extends IAnnotator<?,?>>, Map<IndexToken, Annotation<?>>> annotationsToMap(Map<Class<? extends IAnnotator<?,?>>, List<? extends Annotation<?>>> annotations, Class<? extends IndexToken> index){
		Map<Class<? extends IAnnotator<?,?>>, Map<IndexToken, Annotation<?>>> annoMap = new HashMap<Class<? extends IAnnotator<?,?>>, Map<IndexToken, Annotation<?>>>();
		for(Class<? extends IAnnotator<?,?>> annotator : annotations.keySet()){
			annoMap.put(annotator, annotationsToMap(annotations.get(annotator),index));
		}
		return annoMap;
	}
	
	public static class AnnotationPositionComparator implements Comparator<Annotation<?>> {
		public int compare(Annotation<?> ann1, Annotation<?> ann2) {
			try {
				return ann1.getIndex(PositionIndexToken.class).getPosition() < ann2.getIndex(PositionIndexToken.class).getPosition() ? -1 : ann1.getIndex(PositionIndexToken.class).getPosition() == ann2.getIndex(PositionIndexToken.class).getPosition() ? 0 : 1;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}	
	}
	
	public static class AnnotationOffsetComparator implements Comparator<Annotation<?>> {
		public int compare(Annotation<?> ann1, Annotation<?> ann2) {
			return ann1.getStart() < ann2.getStart() ? -1 : ann1.getStart() == ann2.getStart() ? 0 : 1;
		}
	}
	
}

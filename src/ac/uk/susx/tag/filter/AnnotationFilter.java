package ac.uk.susx.tag.filter;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;

public class AnnotationFilter <AT> implements Filter<AT> {

	public Collection<Annotation<AT>> filter(
			Map<IndexToken, Annotation<AT>> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> filterCollection(
			Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotations) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private final ArrayList<AT> excludeAnnotations;
//	private final Class<? extends Annotator> annotator;
//	private boolean remAll;
//	
//	public AnnotationFilter(Collection<AT> annotations, Class<? extends Annotator> annotator, boolean remAll) {
//		this.excludeAnnotations = new ArrayList<AT>();
//		this.excludeAnnotations.addAll(annotations);
//		this.annotator = annotator;
//		this.remAll = remAll;
//	}
//	
//	public AnnotationFilter(AT annotation, Class<? extends Annotator> annotator, boolean remAll){
//		this(new ArrayList<AT>(), annotator, remAll);
//		excludeAnnotations.add(annotation);
//	}
//	
//	
//	public AnnotationFilter(AT annotation, boolean remAll) {
//		this(annotation, null, remAll);
//	}
//
//	public Collection<Annotation<AT>> filter(
//			Collection<Annotation<AT>> annotations) {
//		Iterator<? extends Annotation<AT>> iter = annotations.iterator();
//		while(iter.hasNext()){
//			Annotation<AT> anno = iter.next();
//			for(AT annotation : excludeAnnotations){
//				if(anno.getAnnotation().equals(annotation)){
//					annotations.remove(anno);
//				}
//			}
//		}
//		return annotations;
//	}
//
//	public Map<Class<? extends Annotator>, Collection<Annotation<AT>>> filterCollection(
//			Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations) {
//		if(!remAll){
//			if(annotator == null){
//				for(Class<? extends Annotator> annotator : annotations.keySet()){
//					annotations.put(annotator, filter(annotations.get(annotator)));
//				}
//			}
//			else{
//				annotations.put(annotator, filter(annotations.get(annotator)));
//			}
//		}
//		else{
//			Map<IndexToken, Collection<Annotation<AT>>> collatedAnnotations = AnnotationUtils.collateAnnotations(annotations);
//			if(annotator == null){
//				for(Class<? extends Annotator> annotator : annotations.keySet()){
//					Iterator<Annotation<AT>> iter = annotations.get(annotator).iterator();
//					while(iter.hasNext()){
//						Annotation<AT> annotation = iter.next();
//						for(AT excludeAnn : excludeAnnotations){
//							if(annotation.getAnnotation().equals(excludeAnn)) {
//								if(!remAll){
//									annotations.remove(annotation);
//								}
//								else{
//									collatedAnnotations.remove(annotation.getOffset());
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return annotations;
//	}
}

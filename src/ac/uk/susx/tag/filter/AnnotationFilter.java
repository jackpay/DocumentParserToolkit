package ac.uk.susx.tag.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;

public class AnnotationFilter <AT> implements Filter<AT> {
	
	private final ArrayList<AT> excludeAnnotations;
	private final Class<? extends Annotator> annotator;
	private boolean remAll;
	
	public AnnotationFilter(Collection<AT> annotations, Class<? extends Annotator> annotator, boolean remAll) {
		this.excludeAnnotations = new ArrayList<AT>();
		this.excludeAnnotations.addAll(annotations);
		this.annotator = annotator;
		this.remAll = remAll;
	}
	
	public AnnotationFilter(AT annotation, Class<? extends Annotator> annotator, boolean remAll){
		this(new ArrayList<AT>(), annotator, remAll);
		excludeAnnotations.add(annotation);
	}
	
	
	public AnnotationFilter(AT annotation, boolean remAll) {
		this(annotation, null, remAll);
	}

	public Collection<Annotation<AT>> filter(
			Collection<Annotation<AT>> annotations) {
		Iterator<? extends Annotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			Annotation<AT> anno = iter.next();
			for(AT annotation : excludeAnnotations){
				if(anno.getAnnotation().equals(annotation)){
					annotations.remove(anno);
				}
			}
		}
		return annotations;
	}

	public Map<Class<? extends Annotator>, Collection<Annotation<AT>>> filterCollection(Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations) {
		if(!remAll){
			annotations.put(annotator, filter(annotations.get(annotator)));
		}
		else{
			Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annoMap = (Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>>) FilterUtils.annotationsToMap(annotations);
			Collection<Annotation<AT>> filtAnno = annotations.get(annotator);
			Iterator<Annotation<AT>> iter = filtAnno.iterator();
			while(iter.hasNext()){
				Annotation<AT> next = iter.next();
			}
		}
		return annotations;
	}
}

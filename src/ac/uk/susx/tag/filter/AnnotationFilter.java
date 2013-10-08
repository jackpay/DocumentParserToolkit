package ac.uk.susx.tag.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;

public class AnnotationFilter <AT> implements Filter<AT> {

	private final AT annotation;
	
	public AnnotationFilter(AT annotation){
		this.annotation = annotation;
	}

	public Collection<Annotation<AT>> filter(Map<IndexToken, Annotation<AT>> annotations) {
		Iterator<Annotation<AT>> iter = annotations.values().iterator();
		while(iter.hasNext()){
			Annotation<AT> next = iter.next();
			if(next.getAnnotation().equals(annotation)){
				iter.remove();
			}
		}
		return annotations.values();
	}

	public Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> filterCollection(
			Map<Class<? extends Annotator>, Map<IndexToken, Annotation<AT>>> annotations) {
		return null;
	}
	
}

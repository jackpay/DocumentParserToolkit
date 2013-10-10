package ac.uk.susx.tag.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;

public class ExcludeAnnotationFilter <AT> implements IFilter<AT> {
	
	private final ArrayList<AT> excludeAnnotations;
	private final Class<? extends IAnnotator> annotator;
	private boolean remAll;
	
	public ExcludeAnnotationFilter(Collection<AT> annotations, Class<? extends IAnnotator> annotator, boolean remAll) {
		this.excludeAnnotations = new ArrayList<AT>();
		this.excludeAnnotations.addAll(annotations);
		this.annotator = annotator;
		this.remAll = remAll;
	}
	
	public ExcludeAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAll){
		this(new ArrayList<AT>(), annotator, remAll);
		excludeAnnotations.add(annotation);
	}
	
	
	public ExcludeAnnotationFilter(AT annotation, boolean remAll) {
		this(annotation, null, remAll);
	}

	public Collection<IAnnotation<AT>> filter(
			Collection<IAnnotation<AT>> annotations) {
		Iterator<? extends IAnnotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			IAnnotation<AT> anno = iter.next();
			for(AT annotation : excludeAnnotations){
				if(anno.getAnnotation().equals(annotation)){
					iter.remove();
				}
			}
		}
		return annotations;
	}

	public Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> filterCollection(Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> annotations) {
		if(!remAll){
			annotations.put(annotator, filter(annotations.get(annotator)));
		}
		else{
			Map<Class<? extends IAnnotator>, Map<IIndexToken, IAnnotation<AT>>> annoMap = FilterUtils.annotationsToMap(annotations, TermOffsetIndexToken.class);
			Collection<IAnnotation<AT>> filtAnno = annotations.get(annotator);
			Iterator<IAnnotation<AT>> iter = filtAnno.iterator();
			while(iter.hasNext()){
				IAnnotation<AT> next = iter.next();
				if(excluded(next.getAnnotation())){
					iter.remove();
					TermOffsetIndexToken index = next.getOffset();
					for(Class<? extends IAnnotator> key : annotations.keySet()){
						if(!key.equals(annotator)){
							annotations.get(key).remove(annoMap.get(key).get(index));
						}
					}
				}
			}
		}
		return annotations;
	}
	
	private boolean excluded(AT annotation){
		boolean excluded = false;
		for(AT exAnn : excludeAnnotations){
			if(annotation.equals(exAnn)){
				return true;
			}
		}
		return excluded;
	}
}

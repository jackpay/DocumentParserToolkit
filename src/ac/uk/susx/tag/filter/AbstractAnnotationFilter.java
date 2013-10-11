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

public abstract class AbstractAnnotationFilter<AT>  implements IFilter<AT>{
	
	private final ArrayList<AT> filterAnnotations;
	private final Class<? extends IAnnotator> annotator;
	private boolean remAllTok;
	private boolean remove;
	
	public AbstractAnnotationFilter(Collection<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok, boolean remove) {
		this.filterAnnotations = new ArrayList<AT>();
		this.filterAnnotations.addAll(filterAnnotations);
		this.annotator = annotator;
		this.remAllTok = remAllTok;
		this.remove = remove;
	}
	
	public AbstractAnnotationFilter(AT annotation, Class<? extends IAnnotator> annotator, boolean remAllTok, boolean remove) {
		this(new ArrayList<AT>(), annotator, remAllTok, remove);
		filterAnnotations.add(annotation);
	}
	
	public Collection<IAnnotation<AT>> filter(Collection<IAnnotation<AT>> annotations) {
		Iterator<? extends IAnnotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			IAnnotation<AT> anno = iter.next();
			if((matchAnnotation(anno.getAnnotation()) && remove) || (!matchAnnotation(anno.getAnnotation()) && !remove)) {
				iter.remove();
			}
		}
		return annotations;
	}
	
	public Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> filterCollection(Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> annotations) {
		if(!remAllTok){
			annotations.put(annotator, filter(annotations.get(annotator)));
		}
		else{
			Map<Class<? extends IAnnotator>, Map<IIndexToken, IAnnotation<AT>>> annoMap = FilterUtils.annotationsToMap(annotations, TermOffsetIndexToken.class);
			Collection<IAnnotation<AT>> filtAnno = annotations.get(annotator);
			Iterator<IAnnotation<AT>> iter = filtAnno.iterator();
			while(iter.hasNext()){
				IAnnotation<AT> next = iter.next();
				if((matchAnnotation(next.getAnnotation()) && remove) || (!matchAnnotation(next.getAnnotation()) && !remove)){
					iter.remove();
					TermOffsetIndexToken index = null;
					try {
						index = next.getIndexToken(TermOffsetIndexToken.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
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
	
	private boolean matchAnnotation(AT annotation){
		boolean match = false;
		for(AT exAnn : filterAnnotations){
			if(annotation.equals(exAnn)){
				return true;
			}
		}
		return match;
	}

}

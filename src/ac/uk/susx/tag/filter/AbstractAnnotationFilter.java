package ac.uk.susx.tag.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;

public abstract class AbstractAnnotationFilter<AT>  implements IFilter<AT>{
	
	private final ArrayList<AT> filterAnnotations;
	private final Class<? extends IAnnotator> annotator;
	private final boolean remAllTok;
	private final boolean remove;
	
	public AbstractAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator> annotator, boolean remAllTok, boolean remove) {
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
	
	public AbstractAnnotationFilter(Class<? extends IAnnotator> annotator, boolean remAllTok, boolean remove) {
		filterAnnotations = null;
		this.annotator = annotator;
		this.remAllTok = remAllTok;
		this.remove = remove;
	}
	
	public List<IAnnotation<AT>> filter(List<IAnnotation<AT>> annotations) {
		Iterator<? extends IAnnotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			IAnnotation<AT> anno = iter.next();
			if((matchAnnotation(anno) && remove) || (!matchAnnotation(anno) && !remove)) {
				iter.remove();
			}
		}
		return annotations;
	}
	
	public Map<Class<? extends IAnnotator>, List<IAnnotation<AT>>> filterCollection(Map<Class<? extends IAnnotator>, List<IAnnotation<AT>>> annotations) {
		if(annotations.get(annotator) == null) {
			return annotations;
		}
		if(!remAllTok){
			annotations.put(annotator, filter(annotations.get(annotator)));
		}
		else{
			Map<Class<? extends IAnnotator>, Map<IIndexToken, IAnnotation<AT>>> annoMap = FilterUtils.annotationsToMap(annotations, TermOffsetIndexToken.class);
			Collection<IAnnotation<AT>> filtAnno = annotations.get(annotator);
			Iterator<IAnnotation<AT>> iter = filtAnno.iterator();
			while(iter.hasNext()){
				IAnnotation<AT> next = iter.next();
				if((matchAnnotation(next) && remove) || (!matchAnnotation(next) && !remove)){
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
	
	public List<AT> getFilterAnnotations() {
		return filterAnnotations;
	}
	
	public abstract boolean matchAnnotation(IAnnotation<AT> annotation);

}

package ac.uk.susx.tag.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;

public class MatchingOffsetFilter implements IFilter<String> {
	
	private final Class<? extends IAnnotator> cl1;
	private final Class<? extends IAnnotator> cl2;
	private boolean remAll;

	public MatchingOffsetFilter(Class<? extends IAnnotator> cl1, Class<? extends IAnnotator> cl2, boolean remAllTok) {
		this.cl1 = cl1;
		this.cl2 = cl2;
		this.remAll = remAllTok;
	}

	public List<IAnnotation<String>> filter(List<IAnnotation<String>> annotations) {
		return null;
	}

	public Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> filterCollection(Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> annotations) {
		if(annotations.get(cl1) == null || annotations.get(cl2) == null) {
			return annotations;
		}
		else{
			Map<Class<? extends IAnnotator>, Map<IIndexToken, IAnnotation<String>>> annoMap = FilterUtils.annotationsToMap(annotations, TermOffsetIndexToken.class);
			Collection<IAnnotation<String>> filtAnnos = annotations.get(cl1);
			Collection<IAnnotation<String>> annos = annotations.get(cl2);
			for(IAnnotation<String> filtAnno : filtAnnos) {
				for(IAnnotation<String> ann : annos){
					if(filtAnno.getStart() == ann.getStart() && filtAnno.getEnd() == ann.getEnd()) {
						if(!remAll) {
							annotations.get(cl2).remove(ann);
						}
						else{
							TermOffsetIndexToken index = null;
							try {
								index = ann.getIndexToken(TermOffsetIndexToken.class);
							} catch (Exception e) {
								e.printStackTrace();
							}
							for(Class<? extends IAnnotator> key : annotations.keySet()){
								if(!key.equals(cl2)){
									annotations.get(key).remove(annoMap.get(key).get(index));
								}
							}
						}
					}
				}
			}
			
		}
		return annotations;
	}
	
	
	
}

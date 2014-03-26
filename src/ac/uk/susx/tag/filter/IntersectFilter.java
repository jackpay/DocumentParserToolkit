package ac.uk.susx.tag.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.indexing.TermOffsetIndexToken;
import ac.uk.susx.tag.utils.FilterUtils;

public class IntersectFilter implements IFilter<String>{

	private Class<? extends IAnnotator> annotator;
	private Class<? extends IAnnotator> nerAnnotator;

	public IntersectFilter(Class<? extends IAnnotator> annotator, Class<? extends IAnnotator> nerAnnotator, boolean remAllTok, boolean remove) {
		this.annotator = annotator;
		this.nerAnnotator = nerAnnotator;
	}

	public List<IAnnotation<String>> filter(List<IAnnotation<String>> annotations) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> filterCollection(Map<Class<? extends IAnnotator>, List<IAnnotation<String>>> annotations) {
		if(annotations.get(annotator) == null || annotations.get(nerAnnotator) == null) {
			return annotations;
		}
		
		Map<Class<? extends IAnnotator>, Map<IIndexToken, IAnnotation<String>>> map = FilterUtils.annotationsToMap(annotations);
		ArrayList<IAnnotation<String>> removeAnnos = new ArrayList<IAnnotation<String>>();
		
		for(IAnnotation<String> nerAnno : annotations.get(nerAnnotator)) {
			for(IAnnotation<String> anno : annotations.get(annotator)) {
				if(FilterUtils.annotationsIntersect(anno, nerAnno)) {
					try {
						IIndexToken index = anno.getIndexToken(TermOffsetIndexToken.class);
						for(Class<? extends IAnnotator> annoT : map.keySet()) {
							if(!annoT.equals(nerAnnotator)) {
								IAnnotation<String> alAnn = annotations.get(annoT).get(annotations.get(annoT).indexOf(map.get(annoT).get(index)));
								System.err.println(alAnn.getAnnotation());
								if(alAnn != null){
									removeAnnos.add(alAnn);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
		return annotations;
		
	}

}

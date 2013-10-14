package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;

public class ReflectionAnnotatorFactory extends AbstractAnnotatorFactory {
	
	public <A extends IAnnotator<?,?,?,?>> A getAnnotator(Class<A> annType){
		IAnnotator<?, ?, ?, ?> annotator = getAnnotators().get(annType);
		if(annotator == null){
			try {
				getAnnotators().put(annType, annType.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		getAnnotators().get(annType).startModel();
		return annType.cast(getAnnotators().get(annType));
	}

}

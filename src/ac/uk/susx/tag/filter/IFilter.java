package ac.uk.susx.tag.filter;

import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

/**
 * A filter interface to allow removal or exclusive inclusion of specific annotations.
 * @author jackpay
 *
 * @param <AT>
 */
public interface IFilter<AT>{
	
	/**
	 * Filter a single set of annotations.
	 * @param annotations
	 * @return
	 */
	public List<IAnnotation<AT>> filter(List<? extends IAnnotation<AT>> annotations);
	
	/**
	 * Filter an entire collection of annotations.
	 * @param annotations
	 * @return
	 */
	public Map<Class<? extends IAnnotator>, List<? extends IAnnotation<AT>>> filterCollection(Map<Class<? extends IAnnotator>, List<? extends IAnnotation<AT>>> annotations);

}

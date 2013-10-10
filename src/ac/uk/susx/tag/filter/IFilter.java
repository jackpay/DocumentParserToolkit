package ac.uk.susx.tag.filter;

import java.util.Collection;
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
	public Collection<IAnnotation<AT>> filter(Collection<IAnnotation<AT>> annotations);
	
	/**
	 * Filter an entire collection of annotations.
	 * @param annotations
	 * @return
	 */
	public Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> filterCollection(Map<Class<? extends IAnnotator>, Collection<IAnnotation<AT>>> annotations);

}

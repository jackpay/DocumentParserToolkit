package ac.uk.susx.tag.filter;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;

/**
 * A filter interface to allow removal or exclusive inclusion of specific annotations.
 * @author jackpay
 *
 * @param <AT>
 */
public interface Filter<AT>{
	
	/**
	 * Filter a single set of annotations.
	 * @param annotations
	 * @return
	 */
	public Collection<Annotation<AT>> filter(Collection<Annotation<AT>> annotations);
	
	/**
	 * Filter an entire collection of annotations.
	 * @param annotations
	 * @return
	 */
	public Map<Class<? extends Annotator>, Collection<Annotation<AT>>> filterCollection(Map<Class<? extends Annotator>, Collection<Annotation<AT>>> annotations);

}

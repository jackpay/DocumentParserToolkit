package ac.uk.susx.tag.filter;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.Sentence;

/**
 * A filter interface to allow removal or exclusive inclusion of specific annotations.
 * @author jackpay
 *
 * @param <AT>
 */
public interface IFilter<AT> {
	
	/**
	 * Filter a single set of annotations.
	 * @param list
	 * @return
	 */
	public List<? extends IAnnotation<AT>> filterList(List<? extends IAnnotation<AT>> list);
	
	/**
	 * Filter an entire collection of annotations.
	 * @param annotations
	 * @return
	 */
	public Sentence filterSentence(Sentence sentence);

}

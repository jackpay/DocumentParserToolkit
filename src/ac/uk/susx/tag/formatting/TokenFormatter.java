package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotation.Annotation;

/**
 * Used to process a collection of individual tokens and output a single token representation of the collection.
 * @author jackpay
 *
 * @param <A>
 */
public interface TokenFormatter<A,AT> {

	public void addToken(Annotation<AT> token);
	
	public A createToken();
	
}

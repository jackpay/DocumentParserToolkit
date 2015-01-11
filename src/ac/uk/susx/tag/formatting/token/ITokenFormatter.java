package ac.uk.susx.tag.formatting.token;

import java.util.Collection;
import ac.uk.susx.tag.annotation.Annotation;

/**
 * Used to process a collection of individual tokens and output a single token representation of the collection.
 * @author jackpay
 *
 * @param <A>
 */
public interface ITokenFormatter<A> {

	public A createToken(Collection<? extends Annotation<?>> tokens);
	
}

package ac.uk.susx.tag.formatting.token;

import ac.uk.susx.tag.annotation.IAnnotation;

public interface IToken<A> {
	
	public String formatForOutput(IAnnotation<A> token);
	
}

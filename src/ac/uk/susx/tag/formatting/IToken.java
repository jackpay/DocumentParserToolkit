package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotation.IAnnotation;

public interface IToken<A> {
	
	public CharSequence formatForOutput(IAnnotation<A> token);
	
}

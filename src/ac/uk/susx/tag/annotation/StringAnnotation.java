package ac.uk.susx.tag.annotation;

import ac.uk.susx.tag.formatting.token.IToken;
import ac.uk.susx.tag.formatting.token.StringToken;


public final class StringAnnotation extends AbstractAnnotation<String> {
	
	private static final IToken<String> formatter = new StringToken();

	public StringAnnotation(String annotation, int start, int end) {
		super(annotation, start, end);
	}
	
	public CharSequence formatForOutput() {
		return formatter.formatForOutput(this);
	}
	
}

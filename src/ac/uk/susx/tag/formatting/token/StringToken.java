package ac.uk.susx.tag.formatting.token;

import ac.uk.susx.tag.annotation.IAnnotation;

public class StringToken implements IToken<String>{

	public String formatForOutput(IAnnotation<String> token) {
		return token.getAnnotation();
	}

}
package ac.uk.susx.tag.annotation;

import uk.ac.susx.tag.dependencyparser.datastructures.Token;

public class TokenAnnotation extends AbstractAnnotation<Token> {

	public TokenAnnotation(Token annotation, int start, int end) {
		super(annotation, start, end);
	}

	@Override
	public String formatForOutput() {
		return getAnnotation().getDeprel();
	}

}

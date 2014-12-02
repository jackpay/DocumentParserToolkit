package ac.uk.susx.tag.annotation;

import uk.ac.susx.tag.dependencyparser.datastructures.Token;

public class DepRelTokenAnnotation extends AbstractAnnotation<Token> {

	public DepRelTokenAnnotation(Token annotation, int start, int end) {
		super(annotation, start, end);
	}

	@Override
	public String formatForOutput() {
		return getAnnotation().getDeprel();
	}

}

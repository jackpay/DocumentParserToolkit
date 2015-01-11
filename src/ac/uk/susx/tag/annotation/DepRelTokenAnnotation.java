package ac.uk.susx.tag.annotation;

import uk.ac.susx.tag.dependencyparser.datastructures.Token;

public class DepRelTokenAnnotation extends Annotation<Token> {

	public DepRelTokenAnnotation(Token annotation, int start, int end) {
		super(annotation, start, end);
	}
	
	public String toString() {
		getAnnotation().getDeprel();
	}

}

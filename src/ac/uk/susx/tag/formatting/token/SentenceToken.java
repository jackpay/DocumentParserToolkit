package ac.uk.susx.tag.formatting.token;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.document.Sentence;

public final class SentenceToken extends Token<Sentence>{

	private final Sentence token;

	public SentenceToken(Sentence token) {
		super(token);
		this.token = token;
	}

	public String formatForOutput(Annotation<Sentence> token) {
		return token.getAnnotation().getSentence().getAnnotation();
	}

	@Override
	public String toString() {
		return token.getSentence().getAnnotation();
	}

}


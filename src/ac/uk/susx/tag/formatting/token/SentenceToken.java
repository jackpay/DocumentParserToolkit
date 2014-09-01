package ac.uk.susx.tag.formatting.token;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.Sentence;

public final class SentenceToken implements IToken<Sentence>{

	public String formatForOutput(IAnnotation<Sentence> token) {
		return token.getAnnotation().getSentence().getAnnotation();
	}

}


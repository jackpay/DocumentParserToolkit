package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.Sentence;

public class SentenceToken implements IToken<Sentence>{

	public String formatForOutput(IAnnotation<Sentence> token) {
		return token.getAnnotation().getSentence().getAnnotation();
	}

}

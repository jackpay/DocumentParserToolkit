package ac.uk.susx.tag.formatting.token;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.TokenAnnotator;

public class BybloTokenFormatter implements ITokenFormatter<CharSequence> {
	
	private static final Class<TokenAnnotator> TOKENISER = TokenAnnotator.class;


	@Override
	public CharSequence createToken(Collection<? extends Annotation<?>> tokens) {
		// TODO Auto-generated method stub
		return null;
	}

}

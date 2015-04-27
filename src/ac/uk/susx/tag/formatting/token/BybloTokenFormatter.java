package ac.uk.susx.tag.formatting.token;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.SentenceOffsetPairAnnotation;

public class BybloTokenFormatter implements ITokenFormatter<CharSequence> {
	
	private static final CharSequence DELIM = "\t";
	private static final CharSequence CON = "/";

	@Override
	public String createToken(Collection<Annotation<?>> tokens) {
		final StringBuilder sb = new StringBuilder();
		for(Annotation<?> a : tokens) {
			if(a instanceof SentenceOffsetPairAnnotation){
				if(a.toString().length() > 0 && a.toString() != null){
					sb.append(DELIM).append(a.toString());
				}
			}
			else{
				sb.append(CON).append(a.toString());
			}
		}
		return sb.substring(1, sb.length());
	}

}

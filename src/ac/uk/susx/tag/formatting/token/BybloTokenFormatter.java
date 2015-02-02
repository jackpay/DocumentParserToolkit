package ac.uk.susx.tag.formatting.token;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.AnnotationListAnnotation;

public class BybloTokenFormatter implements ITokenFormatter<CharSequence> {
	
	private static final CharSequence DELIM = "\t";
	private static final CharSequence CON = "/";

	@Override
	public CharSequence createToken(Collection<Annotation<?>> tokens) {
		final StringBuilder sb = new StringBuilder();
		for(Annotation<?> a : tokens) {
			if(a instanceof AnnotationListAnnotation){
				sb.append(DELIM).append(a.toString());
			}
			else{
				sb.append(a.toString()).append(CON);
			}
		}
		return sb.toString();
	}

}

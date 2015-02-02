package ac.uk.susx.tag.formatting.token;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.AnnotationListAnnotation;

public class StandardTokenFormatter implements ITokenFormatter<CharSequence>{
	
	private final CharSequence ANN_DELIM;
	
	public StandardTokenFormatter(){
		this("_");
	}
	
	public StandardTokenFormatter(CharSequence delimiter){
		ANN_DELIM = delimiter;
	}

	/**
	 * Used to create a token assuming there is no empty annotations contained within the input collection.
	 */
	public CharSequence createToken(Collection<Annotation<?>> tokens) {
		StringBuilder sb = new StringBuilder();
		Iterator<Annotation<?>> iter = tokens.iterator();
		for(int i = 0; i < tokens.size(); i++){
			Annotation<?> token = iter.next();
			sb.append(token.toString());
			if(i < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
		}
		return sb.toString();
	}

}

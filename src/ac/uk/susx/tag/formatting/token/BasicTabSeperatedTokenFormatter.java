package ac.uk.susx.tag.formatting.token;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.IAnnotation;

public class BasicTabSeperatedTokenFormatter implements ITokenFormatter<CharSequence>{
	
	private final CharSequence ANN_DELIM;
	
	public BasicTabSeperatedTokenFormatter(){
		this("-");
	}
	
	public BasicTabSeperatedTokenFormatter(CharSequence delimiter){
		ANN_DELIM = delimiter;
	}

	/**
	 * Used to create a token assuming there is no empty annotations contained within the input collection.
	 */
	public CharSequence createToken(Collection<? extends IAnnotation<?>> tokens) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends IAnnotation<?>> iter = tokens.iterator();
		for(int i = 0; i < tokens.size(); i++){
			IAnnotation<?> token = iter.next();
			sb.append(token.formatForOutput());
			if(i < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
			// Used for debugging purposes!
//			else{
//				sb.append("_").append(token.getStart()).append("_").append(token.getEnd());
//			}
		}
		return sb.toString();
	}

}

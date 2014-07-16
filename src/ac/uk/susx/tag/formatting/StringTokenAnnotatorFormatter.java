package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.IAnnotation;

public class StringTokenAnnotatorFormatter implements TokenFormatter<String, String>{
	
	private final String ANN_DELIM;
	
	public StringTokenAnnotatorFormatter(){
		this("_");
	}
	
	public StringTokenAnnotatorFormatter(String delimiter){
		ANN_DELIM = delimiter;
	}

	/**
	 * Used to create a token assuming there are no empty annotations contained within the input collection.
	 */
	public String createToken(Collection<? extends IAnnotation<String>> tokens) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends IAnnotation<String>> iter = tokens.iterator();
		for(int i = 0; i < tokens.size(); i++){
			IAnnotation<String> next = iter.next();
			//Used for debugging 			//.append(ANN_DELIM).append(next.getStart()+"-"+next.getEnd());
			sb.append(next.getAnnotation());
			if(i < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
		}
		return sb.toString();
	}

}

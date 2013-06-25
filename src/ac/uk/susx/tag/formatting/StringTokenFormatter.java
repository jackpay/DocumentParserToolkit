package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.Annotation;

public class StringTokenFormatter implements TokenFormatter<String, String>{
	
	private static final String ANN_DELIM = "-";

	public String createToken(Collection<? extends Annotation<String>> tokens) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends Annotation<String>> iter = tokens.iterator();
		for(int i = 0; i < tokens.size(); i++){
			sb.append(iter.next());
			if(i < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
		}
		return sb.toString();
	}

}

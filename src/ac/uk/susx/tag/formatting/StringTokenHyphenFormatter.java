package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.Annotation;

public class StringTokenHyphenFormatter implements TokenFormatter<String, String>{
	
	private static final String ANN_DELIM = "-";

	/**
	 * Used to create a token assuming there is no empty annotations contained within the input collection.
	 */
	public String createToken(Collection<? extends Annotation<String>> tokens) {
		StringBuilder sb = new StringBuilder();
		Iterator<? extends Annotation<String>> iter = tokens.iterator();
		for(int i = 0; i < tokens.size(); i++){
			sb.append(iter.next().getAnnotation());
			if(i < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Used to create a token. Passes the string document in order to allow offset annotations to be used.
	 * @param document
	 * @param tokens
	 * @return
	 */
	public String createToken(String document, Collection<? extends Annotation<String>> tokens){
		StringBuilder sb = new StringBuilder();
		Iterator<? extends Annotation<String>> iter = tokens.iterator();
		int pos = 0;
		while(iter.hasNext()){
			Annotation<String> ann = iter.next();
			String strAnn = ann.getAnnotation();
			if(strAnn == null){
				sb.append(document.substring(ann.getStart(), ann.getEnd()));
			}
			else{
				sb.append(strAnn);
			}
			if(pos < tokens.size()-1){
				sb.append(ANN_DELIM);
			}
			pos++;
		}
		return sb.toString();
	}

}

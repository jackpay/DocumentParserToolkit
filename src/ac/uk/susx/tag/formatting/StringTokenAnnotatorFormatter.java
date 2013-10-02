package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Iterator;

import ac.uk.susx.tag.annotation.Annotation;

public class StringTokenAnnotatorFormatter implements TokenFormatter<String, String>{
	
	private final char ANN_DELIM;
	
	public StringTokenAnnotatorFormatter(){
		this('-');
	}
	
	public StringTokenAnnotatorFormatter(char delimiter){
		ANN_DELIM = delimiter;
	}
	
	public String createToken(Collection<Annotation<String>> annotations) {
		StringBuilder sb = new StringBuilder();
		Iterator<Annotation<String>> iter = annotations.iterator();
		while(iter.hasNext()){
			sb.append(iter.next().getAnnotation());
			if(iter.hasNext()){
				sb.append(ANN_DELIM);
			}
		}
		return sb.toString();
	}
	
}

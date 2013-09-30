package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotation.Annotation;

public class StringTokenAnnotatorFormatter implements TokenFormatter<String, String>{
	
	private final String ANN_DELIM;
	private final StringBuilder sb;
	
	public StringTokenAnnotatorFormatter(){
		this("-");
	}
	
	public StringTokenAnnotatorFormatter(String delimiter){
		ANN_DELIM = delimiter;
		sb = new StringBuilder();
	}
	
	public String createToken() {
		return sb.substring(0, sb.length()-1);
	}

	public void addToken(Annotation<String> token) {
		sb.append(token.getAnnotation());
		sb.append(ANN_DELIM);
	}
}

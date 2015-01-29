package ac.uk.susx.tag.annotation;

import java.util.List;

import ac.uk.susx.tag.formatting.token.StandardTokenFormatter;

public class AnnotationListAnnotation extends Annotation<List<Annotation<?>>>{
	
	private final CharSequence DELIM;
	private final StandardTokenFormatter tokenMaker;

	public AnnotationListAnnotation(List<Annotation<?>> annotation, int start,int end) {
		super(annotation, start, end);
		this.DELIM = "_";
		tokenMaker = new StandardTokenFormatter("/");
	}
	
	public AnnotationListAnnotation(List<Annotation<?>> annotation, CharSequence delim,int start,int end) {
		super(annotation, start, end);
		this.DELIM = delim;
		tokenMaker = new StandardTokenFormatter(delim);
	}

	@Override
	public String toString() {
		for(Annotation<?> ann : this.getAnnotation()) {
			System.out.println(ann.getAnnotation().toString());
		}
		return tokenMaker.createToken(this.getAnnotation()).toString();
	}

}

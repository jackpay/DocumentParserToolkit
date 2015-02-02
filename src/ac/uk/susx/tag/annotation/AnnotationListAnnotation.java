package ac.uk.susx.tag.annotation;

import java.util.List;

import ac.uk.susx.tag.formatting.token.StandardTokenFormatter;

public class AnnotationListAnnotation extends Annotation<List<Annotation<?>>>{
	
	private final CharSequence DELIM;
	private final StandardTokenFormatter tokenMaker;

	public AnnotationListAnnotation(List<Annotation<?>> annotation, int start,int end) {
		super(annotation, start, end);
		this.DELIM = "/";
		tokenMaker = new StandardTokenFormatter("/");
	}
	
	public AnnotationListAnnotation(List<Annotation<?>> annotation, CharSequence delim,int start,int end) {
		super(annotation, start, end);
		this.DELIM = delim;
		tokenMaker = new StandardTokenFormatter(delim);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for(Annotation<?> an : this.getAnnotation()) {
			if(!(an instanceof AnnotationListAnnotation)) {
				sb.append(an.toString() + DELIM);
			}
		}
		return sb.toString();
	}

}

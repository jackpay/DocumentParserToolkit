package ac.uk.susx.tag.annotation;

public final class StringAnnotation extends Annotation<String> {

	public StringAnnotation(String annotation, int start, int end) {
		super(annotation, start, end);
	}

	@Override
	public String toString() {
		return this.getAnnotation();
	}
	
}

package ac.uk.susx.tag.annotation.annotations;


public class GrammaticalAnnotation extends AbstractAnnotation<String, String>{

	public GrammaticalAnnotation(String annotation, String token, int pos,
			int start, int end) {
		super(annotation, token, pos, start, end);
	}
	
}

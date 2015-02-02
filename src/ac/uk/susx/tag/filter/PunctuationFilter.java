package ac.uk.susx.tag.filter;
import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class PunctuationFilter extends AbstractAnnotationFilter<String> {
	

	private static final String[] punc = {",",":",".",":",";","-","?","(",")","[","]","'","\"","<",">","%"};
	
	public PunctuationFilter(Class<? extends IAnnotator<String, ?>> annotator) {
		super(annotator, true, true);
	}

	@Override
	public boolean matchAnnotation(Annotation<String> annotation) {
		for(String p : punc) {
			if(annotation.getAnnotation().equals(p)) {
				return true;
			}
		}
		return false;
	}

}

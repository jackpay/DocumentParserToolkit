package ac.uk.susx.tag.filter;

import java.util.List;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public class CanonicaliseFilter<AT> implements IFilter<AT> {
	
	private final AT swapToken;
	private final Class<? extends IAnnotator<AT,?>> annotator;
	private final String pattern;
	
	public CanonicaliseFilter(AT swapToken, String pattern,Class<? extends IAnnotator<AT,?>> annotator) {
		this.swapToken = swapToken;
		this.annotator = annotator;
		this.pattern = pattern;
	}

	@Override
	public List<Annotation<AT>> filterList(List<Annotation<AT>> list) {
		for(Annotation<AT> anno : list) {
			if(Pattern.matches(pattern, anno.toString())){
				anno.setAnnotation(swapToken);
			}
		}
		return list;
	}

	@Override
	public Sentence filterSentence(Sentence sentence) {
		try {
			filterList(sentence.getSentenceAnnotations(annotator));
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		return sentence;
	}

}

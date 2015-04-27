package ac.uk.susx.tag.filter;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.ContextWindowAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.OffsetIndexToken;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public class VerbAndLeftNounFilter implements IFilter<String> {
	
	
	private Class<ContextWindowAnnotator> annotator = ContextWindowAnnotator.class;
	private Class<PoSTagAnnotator> postagger = PoSTagAnnotator.class;
	private static final String NOUN = "N";
	private static final String VERB = "V";

	@Override // Returns null as cannot be implemented.
	public List<? extends Annotation<String>> filterList(List<? extends Annotation<String>> list) {
		return null;
	}

	@Override
	public Sentence filterSentence(Sentence sentence) {
		try {
			List<Annotation<Pair<Sentence, OffsetIndexToken>>> anns = sentence.getSentenceAnnotations(annotator);
			for(int i = 0; i < anns.size(); i++) {
				Pair<Sentence, OffsetIndexToken> ann = anns.get(i).getAnnotation();
				for(Annotation<String> pos : ann.getLeft().getSentenceAnnotations(postagger)) {
					if(!pos.getAnnotation().startsWith(NOUN) || !pos.getAnnotation().startsWith(VERB)) {
						sentence.removeAnnotation(ann.getRight());
					}
				}
			} //FIX THIS	
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		return sentence;
	}

}

package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.AnnotationListAnnotation;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ContextWindowAnnotator extends AbstractAnnotator<List<Annotation<?>>,String> {
	
	private final int windowSize;
	private static final Class<TokenAnnotator> DEFAULT_ANNOTATOR = TokenAnnotator.class;
	private final List<Class<IAnnotator<String,String>>> annotators;
	
	public ContextWindowAnnotator(int windowSize) {
		this.windowSize = windowSize;
		this.annotators = null;
	}
	public ContextWindowAnnotator(int windowSize, List<Class<IAnnotator<String,String>>> annotators) {
		this.windowSize = windowSize;
		this.annotators = annotators;
	}

	@Override
	public List<Annotation<List<Annotation<?>>>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<Class<IAnnotator<String,String>>> contextAnnos = (List<Class<IAnnotator<String, String>>>) ((annotators == null) ? Arrays.asList(DEFAULT_ANNOTATOR) : annotators);
		for(Class<IAnnotator<String,String>> annotator : contextAnnos) {
			try {
				List<Annotation<String>> annos = sentence.getSentenceAnnotations(DEFAULT_ANNOTATOR);
				for(int i = 0; i < annos.size(); i++){
					sentence.addAnnotations(this.getClass(), getContextWindow(annos, sentence, i));
				}
			} catch (IllegalAnnotationStorageException e) {
				e.printStackTrace();
			}
		}
		try {
			return sentence.getSentenceAnnotations(this.getClass());
		} catch (IllegalAnnotationStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private List<AnnotationListAnnotation> getContextWindow(List<Annotation<String>> annotations, Sentence sentence, int index) {
		int start = index-windowSize;
		int end = index+(windowSize+1);

		end = end > annotations.size() ?  annotations.size() : end;
		start = start < 0 ? 0 : start;
		
		List<Annotation<String>> subList = new ArrayList<>(annotations.subList(start, end));
		subList.remove(annotations.get(index));
		List<AnnotationListAnnotation> window = new ArrayList<AnnotationListAnnotation>();
		for(Annotation<String> anno : subList) {
			AnnotationListAnnotation ala = new AnnotationListAnnotation(sentence.getIndexedAnnotations(anno.getOffset()),annotations.get(index).getStart(), annotations.get(index).getEnd());
			System.err.println(ala.toString());
			window.add(ala);
		}
		return window;
	}

	@Override
	/**
	 * Unimplmented as not possible!!!
	 * @param annotation
	 * @return
	 * @throws IncompatibleAnnotationException
	 */
	public List<Annotation<List<Annotation<?>>>> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException {
		return null;
	}

	@Override
	public void startModel() { }

	@Override
	public boolean modelStarted() { return true; }

}

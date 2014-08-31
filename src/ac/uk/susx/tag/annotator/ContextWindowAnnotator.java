package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ContextWindowAnnotator extends AbstractAnnotator<String,String> {
	
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
	public List<? extends IAnnotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<Class<IAnnotator<String,String>>> contextAnnos = (List<Class<IAnnotator<String, String>>>) ((annotators == null) ? Arrays.asList(DEFAULT_ANNOTATOR) : annotators);
		for(Class<IAnnotator<String,String>> annotator : contextAnnos) {
			try {
				List<IAnnotation<String>> annos = sentence.getSentenceAnnotations(DEFAULT_ANNOTATOR);
				for(int i = 0; i < annos.size(); i++){
					sentence.addAnnotations(this.getClass(), getContextWindow(annos,i));
				}
			} catch (IllegalAnnotationStorageException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private List<IAnnotation<String>> getContextWindow(List<IAnnotation<String>> annotations, int index) {
		int start = index-windowSize;
		int end = index+(windowSize+1);

		end = end > annotations.size() ?  annotations.size() : end;
		start = start < 0 ? 0 : start;
		
		List<IAnnotation<String>> subList = annotations.subList(start, end);
		IAnnotation<String> current = subList.remove((subList.size()/2));
		List<IAnnotation<String>> window = new ArrayList<IAnnotation<String>>();
		for(IAnnotation<String> anno : subList) {
			StringAnnotation sa = new StringAnnotation(anno.getAnnotation(),current.getStart(),current.getEnd());
			window.add(sa);
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
	public List<? extends IAnnotation<String>> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		return null;
	}

	@Override
	public void startModel() { }

	@Override
	public boolean modelStarted() { return true; }

}

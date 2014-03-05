package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.database.TermFrequencyIndexer;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * Annotates a term with its corpus wide term frequency.
 * @author jp242
 *
 */
public class TermFrequencyAnnotator extends AbstractAnnotator<String,String> {
	
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	private TermFrequencyIndexer indexer;
	private static final String PREFIX = "<tf>";
	private static final String SUFFIX = "</tf>";
	
	public TermFrequencyAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser) {
		this.tokeniser = tokeniser;
	}

	public List<? extends IAnnotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		Class<? extends IAnnotator<String, ?>> anno = null;
		try {
			anno = (Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(tokeniser).getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<? extends IAnnotation<String>> tokens = null;
		try {
			tokens = sentence.getSentenceAnnotations(anno);
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		if(tokens == null) {
			try {
				tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence.getSentence());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		for(IAnnotation<String> token : tokens) {
			try {
				annotations.add(new StringAnnotation(new StringBuilder().append(PREFIX).append(indexer.getPrimaryIndex().get(token.getAnnotation()).getFrequency()).append(SUFFIX).toString(),token.getStart(),token.getEnd()));
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		sentence.addAnnotations(this.getClass(),annotations);
		return tokens;
	}

	public List<? extends IAnnotation<String>> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		return null;
	}
	
	public void setIndexer(TermFrequencyIndexer indexer) {
		this.indexer = indexer;
	}

	/**
	 * Not needed.
	 */
	public void startModel() {}

	/**
	 * Always returns true.
	 */
	public boolean modelStarted() {
		return true;
	}

}

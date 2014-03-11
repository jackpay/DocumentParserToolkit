package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.database.TFDFIndexer;
import ac.uk.susx.tag.database.TermFrequencyIndexer;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * Annotates a term with its corpus wide term frequency.
 * @author jp242
 *
 */
public class FrequencyAnnotator extends AbstractAnnotator<String,String> {
	
	
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	private TFDFIndexer indexer;
	private final List<Frequency> freqAnnotations;
	public static final String TF_PREFIX = "<tf>"; 		// Term frequency prefix and suffix
	public static final String TF_SUFFIX = "</tf>";
	public static final String DF_PREFIX = "<df>"; 		// Document frequency prefix and suffix
	public static final String DF_SUFFIX = "</df>";
	public static final String IDF_PREFIX = "<idf>"; 	// Inverse document frequency prefix and suffix
	public static final String IDF_SUFFIX = "</idf>";
	public static final String TFIDF_PREFIX = "<tfidf>";// TFIDF prefix and suffix
	public static final String TFIDF_SUFFIX = "</tfidf>";
	public static final String CF_PREFIX = "<cf>"; 		// Corpus frequency prefix and suffix
	public static final String CF_SUFFIX = "</cf>";
	
	public static enum Frequency {
		TF,
		DF,
		IDF,
		CF,
		TF_IDF;
	}
	
	public void addFrequencyAnnotation(Frequency freq) {
		freqAnnotations.add(freq);
	}
	
	public FrequencyAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser) {
		freqAnnotations = new ArrayList<Frequency>();
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
			double df = 0.0;
			double n = 0.0;
			double tf = 0.0;
			double idf = 0.0;
			try {
				df = indexer.getCorpusFrequencyIndex().get(token.getAnnotation()).getAppearingDocIds().size();
				n = indexer.getTermFrequencyIndex().count();
				tf = indexer.getTermFrequencyIndex().get(sentence.docReference().toString()).getFrequency(token.getAnnotation());
				idf = Math.log(n/df);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
			for(Frequency freq : freqAnnotations) {
				switch(freq) {
					case CF:
						try {
							annotations.add(new StringAnnotation(new StringBuilder().append(CF_PREFIX).append(indexer.getCorpusFrequencyIndex().get(token.getAnnotation()).getFrequency()).append(CF_SUFFIX).toString(),token.getStart(),token.getEnd()));
						} catch (DatabaseException e) {
							e.printStackTrace();
						}
						break;
					case DF:
						annotations.add(new StringAnnotation(new StringBuilder().append(DF_PREFIX).append(df).append(DF_SUFFIX).toString(),token.getStart(),token.getEnd()));
						break;
					case IDF:
						annotations.add(new StringAnnotation(new StringBuilder().append(IDF_PREFIX).append(idf).append(IDF_SUFFIX).toString(),token.getStart(),token.getEnd()));
						break;
					case TF:
						annotations.add(new StringAnnotation(new StringBuilder().append(TF_PREFIX).append(tf).append(TF_SUFFIX).toString(),token.getStart(),token.getEnd()));
						break;
					case TF_IDF:
						double tfidf = tf*idf;
						annotations.add(new StringAnnotation(new StringBuilder().append(TFIDF_PREFIX).append(tfidf).append(TFIDF_SUFFIX).toString(),token.getStart(),token.getEnd()));
						break;
					default:
						break;
				}
			}
		}
		sentence.addAnnotations(this.getClass(),annotations);
		return tokens;
	}

	public List<? extends IAnnotation<String>> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		return null;
	}
	
	public TFDFIndexer getIndexer() {
		return indexer;
	}

	/**
	 * Not needed.
	 */
	public void startModel() {
		if(!modelStarted()) {
			indexer = new TFDFIndexer();
		}
	}

	/**
	 * Always returns true.
	 */
	public boolean modelStarted() {
		return indexer != null;
	}
	
	public static double getDoubleValue(String annotation) {
		return Double.valueOf(annotation.replaceAll("<[a-zA-Z]*", "").replace("/[a-zA-Z]*>", ""));
	}

}

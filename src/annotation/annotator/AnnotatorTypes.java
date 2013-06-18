package annotation.annotator;

import annotation.annotators.ChunkSpanAnnotator;
import annotation.annotators.ChunkTagAnnotator;
import annotation.annotators.PoSTagAnnotator;
import annotation.annotators.SentenceAnnotator;
import annotation.annotators.TokenAnnotator;

/**
 * A enum used to reference the different possible annotators available.
 * @author jackpay
 *
 */
public enum AnnotatorTypes {
	
	POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
	
	CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
	
	CHUNKSPAN(new ChunkSpanAnnotator()), // Used to specify a chunkspan annotator.
	
	TOKEN(new TokenAnnotator()), // Used to specify a token annotator.
	
	SENTENCE(new SentenceAnnotator()); // Used to specify a Sentence annotator.
	
	private final Annotator annotator;

	private AnnotatorTypes(Annotator annotator){
		this.annotator = annotator;
	}
	
	public Annotator getAnnotator(){
		return annotator;
	}
}

package ac.uk.susx.tag.configuration;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.annotator.ChunkSpanAnnotator;
import ac.uk.susx.tag.annotator.ChunkTagAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.SentenceAnnotator;
import ac.uk.susx.tag.annotator.TokenAnnotator;
import ac.uk.susx.tag.document.Document;

public class GrammaticalConfiguration extends AbstractConfiguration <Document<String,String>,String>{

	public GrammaticalConfiguration(String inputLoc, String outputLoc) {
		super(inputLoc, outputLoc);
	}
	
	public enum AnnotatorTypes {
		
		POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
		
		CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
		
		CHUNKSPAN(new ChunkSpanAnnotator()), // Used to specify a chunkspan annotator.
		
		TOKEN(new TokenAnnotator()), // Used to specify a token annotator.
		
		SENTENCE(new SentenceAnnotator()); // Used to specify a Sentence annotator.
		
		private final Annotator<Document<String,String>,? extends Annotation<String>, String> annotator;
		
		private AnnotatorTypes(Annotator<Document<String, String>,? extends Annotation<String>, String> annotator){
			this.annotator = annotator;
		}
		
		public Annotator<Document<String,String>, ? extends Annotation<String>, String> getAnnotator(){
			annotator.startModel();
			return annotator;
		}
		
	}
}

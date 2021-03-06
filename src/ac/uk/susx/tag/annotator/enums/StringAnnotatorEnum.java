package ac.uk.susx.tag.annotator.enums;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.ChunkSpanAnnotator;
import ac.uk.susx.tag.annotator.EntityLinkingAnnotator;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.ChunkTagAnnotator;
import ac.uk.susx.tag.annotator.LemmatiserAnnotator;
import ac.uk.susx.tag.annotator.LocationAnnotator;
import ac.uk.susx.tag.annotator.OrganisationAnnotator;
import ac.uk.susx.tag.annotator.PersonAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.PorterStemmerAnnotator;
import ac.uk.susx.tag.annotator.SentenceAnnotator;
import ac.uk.susx.tag.annotator.TokenAnnotator;
import ac.uk.susx.tag.document.IDocument;

public enum StringAnnotatorEnum {
		
		POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
		
		CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
		
		CHUNKSPAN(new ChunkSpanAnnotator()),
		
		TOKEN(new TokenAnnotator(false)), // Used to specify a token annotator.
		
		SENTENCE(new SentenceAnnotator()), // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
		
		ORGANISATION(new OrganisationAnnotator()), // Used to specify a Person annotator.
		
		PERSON(new PersonAnnotator()),
		
		LOCATION(new LocationAnnotator()),
		
		//LEMMATISER(new LemmatiserAnnotator()),
		
		STEMMER(new PorterStemmerAnnotator()),
		
		ENTITY(new EntityLinkingAnnotator());
		
		private final IAnnotator<IDocument<String,String>,? extends IAnnotation<String>,String,String> annotator;
		
		private StringAnnotatorEnum(IAnnotator<IDocument<String, String>,? extends IAnnotation<String>, String,String> annotator){
			this.annotator = annotator;
		}
		
		public IAnnotator<IDocument<String,String>, ? extends IAnnotation<String>, String,String> getAnnotator(){
			annotator.startModel();
			return annotator;
		}
		
}

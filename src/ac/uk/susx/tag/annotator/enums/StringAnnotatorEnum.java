package ac.uk.susx.tag.annotator.enums;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.annotator.ChunkTagAnnotator;
import ac.uk.susx.tag.annotator.LocationAnnotator;
import ac.uk.susx.tag.annotator.OffsetSentenceAnnotator;
import ac.uk.susx.tag.annotator.OffsetTokenAnnotator;
import ac.uk.susx.tag.annotator.OrganisationAnnotator;
import ac.uk.susx.tag.annotator.PersonAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.StoredSentenceAnnotator;
import ac.uk.susx.tag.annotator.StoredTokenAnnotator;
import ac.uk.susx.tag.document.Document;

public enum StringAnnotatorEnum {
		
		POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
		
		CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
		
		OFFSET_TOKEN(new OffsetTokenAnnotator()), // Used to specify a token annotator.
		
		STORED_TOKEN(new StoredTokenAnnotator()),
		
		OFFSET_SENTENCE(new OffsetSentenceAnnotator()), // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
		
		STORED_SENTENCE(new StoredSentenceAnnotator()), // Used to specify a Sentence annotator which actually stores the sentence string.
		
		ORGANISATION(new OrganisationAnnotator()), // Used to specify a Person annotator.
		
		PERSON(new PersonAnnotator()),
		
		LOCATION(new LocationAnnotator());
		
		private final Annotator<Document<String,String>,? extends Annotation<String>,String,String> annotator;
		
		private StringAnnotatorEnum(Annotator<Document<String, String>,? extends Annotation<String>, String,String> annotator){
			this.annotator = annotator;
		}
		
		public Annotator<Document<String,String>, ? extends Annotation<String>, String,String> getAnnotator(){
			annotator.startModel();
			return annotator;
		}
		
}

package ac.uk.susx.tag.annotator.enums;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.annotator.ChunkTagAnnotator;
import ac.uk.susx.tag.annotator.LocationAnnotator;
import ac.uk.susx.tag.annotator.OrganisationAnnotator;
import ac.uk.susx.tag.annotator.PersonAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.SentenceAnnotator;
import ac.uk.susx.tag.annotator.TokenAnnotator;
import ac.uk.susx.tag.document.Document;

public enum StringAnnotatorEnum {
		
		POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
		
		CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
		
		TOKEN(new TokenAnnotator()), // Used to specify a token annotator.
		
		SENTENCE(new SentenceAnnotator()), // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
		
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

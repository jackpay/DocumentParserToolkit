package ac.uk.susx.tag.annotator.factory;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.ChunkTagAnnotator;
import ac.uk.susx.tag.annotator.LocationAnnotator;
import ac.uk.susx.tag.annotator.OrganisationAnnotator;
import ac.uk.susx.tag.annotator.PersonAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.StringSentenceAnnotator;
import ac.uk.susx.tag.annotator.TokenAnnotator;

public enum StringAnnotatorEnum {

		POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.

		CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.

		TOKEN(new TokenAnnotator()), // Used to specify a token annotator.

		SENTENCE(new StringSentenceAnnotator()), // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).

		ORGANISATION(new OrganisationAnnotator()), // Used to specify a Person annotator.

		PERSON(new PersonAnnotator()),

		LOCATION(new LocationAnnotator());

		private final IAnnotator<?,?,?> annotator;

		private StringAnnotatorEnum(IAnnotator<?,?,?> annotator){
			this.annotator = annotator;
		}

		public IAnnotator<?,?,?> getAnnotator(){
			annotator.startModel();
			return annotator;
		}

}
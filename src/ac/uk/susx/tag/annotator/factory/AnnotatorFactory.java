package ac.uk.susx.tag.annotator.factory;

import java.util.HashMap;

import ac.uk.susx.tag.annotator.IAnnotator;

public class AnnotatorFactory {
	
	private final HashMap<Class<? extends IAnnotator>, IAnnotator> annotators;
	
	public AnnotatorFactory(){
		annotators = new HashMap<Class<? extends IAnnotator>, IAnnotator>();
	}
	
	
	
	public enum AnnotatorEnum{
		POSTAG, // Used to specify a postag annotator.
		CHUNKTAG, // Used to specify a chunktag annotator.
		TOKEN, // Used to specify a token annotator.
		SENTENCE, // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
		ORGANISATION, // Used to specify a Organisation annotator.
		PERSON, // Used to specify a Person annotator.
		LOCATION;// Used to specify a Location annotator.
	}

}

package ac.uk.susx.tag.annotator.registry;

import java.util.HashMap;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotator.IAnnotator;

public final class AnnotatorRegistry {
	
	private static final HashMap<String,IAnnotator<?,?,?>> registry = Maps.newHashMap();

	public static IAnnotator<?, ?, ?> getAnnotator(String id) throws Exception {
		if(registry.get(id) != null) {
			return registry.get(id); 
		}
		else {
			throw new Exception("There is no registered IAnnotator for the given id.");
		}
	}

	public static void register(String key, IAnnotator<?, ?, ?> annotator) {
		registry.put(key, annotator);
	}
	
	public enum AnnotatorEnum {

		POSTAG, // Used to specify a postag annotator.

		CHUNKTAG, // Used to specify a chunktag annotator.
		
		CHUNKSPAN, // Used to specify a chunk span annotator.

		TOKEN, // Used to specify a token annotator.

		SENTENCE, // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).

		ORGANISATION, // Used to specify an Organisation annotator.

		PERSON, // Used to specify a Person annotator

		LOCATION; // Used to specify a Location annotator

}

}
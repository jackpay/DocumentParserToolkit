package ac.uk.susx.tag.annotator.factory;

import java.util.HashMap;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.annotator.PoSTagAnnotator;

public abstract class AbstractAnnotatorFactory {

	private final HashMap<Class<? extends IAnnotator>, IAnnotator<?,?,?,?>> annotators;
	
	public AbstractAnnotatorFactory(){
		annotators = new HashMap<Class<? extends IAnnotator>, IAnnotator<?,?,?,?>>();
		annotators.put(PoSTagAnnotator.class, new PoSTagAnnotator());
	}
	
	protected HashMap<Class<? extends IAnnotator>, IAnnotator<?,?,?,?>> getAnnotators(){
		return annotators;
	}
	
	
	
	public abstract <A extends IAnnotator<?,?,?,?>> A getAnnotator(Class<A> annType);
	
//	public enum AnnotatorEnum{
//		POSTAG, // Used to specify a postag annotator.
//		CHUNKTAG, // Used to specify a chunktag annotator.
//		TOKEN, // Used to specify a token annotator.
//		SENTENCE, // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
//		ORGANISATION, // Used to specify a Organisation annotator.
//		PERSON, // Used to specify a Person annotator.
//		LOCATION;// Used to specify a Location annotator.
//	}
}

//public enum StringAnnotatorEnum{
//	
//	POSTAG(new PoSTagAnnotator()), // Used to specify a postag annotator.
//	
//	CHUNKTAG(new ChunkTagAnnotator()), // Used to specify a chunktag annotator.
//	
//	TOKEN(new TokenAnnotator()), // Used to specify a token annotator.
//	
//	SENTENCE(new SentenceAnnotator()), // Used to specify a Sentence annotator which does not store the sentence string as an annotation (allows use of the document offsets instead to save memory).
//	
//	ORGANISATION(new OrganisationAnnotator()), // Used to specify a Person annotator.
//	
//	PERSON(new PersonAnnotator()),
//	
//	LOCATION(new LocationAnnotator());
//	
//	private final IAnnotator<IDocument<?>,? extends IAnnotation<?>,?,?> annotator;
//	
//	private StringAnnotatorEnum(IAnnotator<IDocument<?>,? extends IAnnotation<?>, ?,?> annotator){
//		this.annotator = annotator;
//	}
//	
//	public IAnnotator<IDocument<?>, ? extends IAnnotation<?>, ?,?> getAnnotator(){
//		annotator.startModel();
//		return annotator;
//	}
//	
//}
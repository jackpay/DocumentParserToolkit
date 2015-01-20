package ac.uk.susx.tag.annotator;

import java.util.Iterator;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractAnnotator <AT,ACT> implements IAnnotator<AT,ACT> {
	
	private Class<? extends IAnnotatorFactory<Sentence,String>> sentence = SentenceAnnotatorFactory.class;
	
	public Document annotate(Document document) throws IncompatibleAnnotationException {
		if(document.isEmpty()){
			try {
				AnnotatorRegistry.getAnnotator(sentence).annotate(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Iterator<Sentence> sentences = document.iterator();
		while(sentences.hasNext()){
			annotate(sentences.next());
		}
		return document;
	}

}

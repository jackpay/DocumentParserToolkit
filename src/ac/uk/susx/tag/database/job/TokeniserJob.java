package ac.uk.susx.tag.database.job;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.TokenAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Document;

public class TokeniserJob implements IJob<IAnnotation<String>> {
	
	public static Class<? extends IAnnotatorFactory<String,String>> tokeniser = TokenAnnotatorFactory.class;
	private final Document doc;
	
	public TokeniserJob(Document doc) {
		this.doc = doc;
	}

	public List<IAnnotation<String>> process() {
		try {
            return (List<IAnnotation<String>>) AnnotatorRegistry.getAnnotator(tokeniser).annotate(new StringAnnotation(doc.getDocument().toString(),0,0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setTokeniser(Class<? extends IAnnotatorFactory<String,String>> cl) {
		tokeniser = cl;
	}

}

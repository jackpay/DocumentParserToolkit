package ac.uk.susx.tag.preparser;

import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.TokenAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.database.DocFreqUnigramEntity;
import ac.uk.susx.tag.document.Document;

public class DocFreqUnigramJob implements IJob<DocFreqUnigramEntity> {
	
	public static Class<? extends IAnnotatorFactory<String,String>> tokeniser = TokenAnnotatorFactory.class;
	private final Document doc;
	
	public DocFreqUnigramJob(Document doc) {
		this.doc = doc;
	}

	public List<DocFreqUnigramEntity> process() {
		ArrayList<DocFreqUnigramEntity> entities = new ArrayList<DocFreqUnigramEntity>();
		try {
			for(IAnnotation<String> token : AnnotatorRegistry.getAnnotator(tokeniser).annotate(new StringAnnotation(doc.getDocument().toString(),0,0))){
				DocFreqUnigramEntity ue = new DocFreqUnigramEntity(token.getAnnotation().toString(),doc.getDocumentId().toString());
				entities.add(ue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entities;
	}
	
	public void setTokeniser(Class<? extends IAnnotatorFactory<String,String>> cl) {
		tokeniser = cl;
	}

}

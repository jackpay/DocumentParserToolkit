package ac.uk.susx.tag.preparser;

import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.TokenAnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.database.UnigramEntity;

public class UnigramJob implements IJob<UnigramEntity>{
	
	public static Class<? extends IAnnotatorFactory<String,String>> tokeniser = TokenAnnotatorFactory.class;
	private CharSequence document;
	
	public UnigramJob(CharSequence document) {
		this.document = document;
	}

	public List<UnigramEntity> process() {
		ArrayList<UnigramEntity> entities = new ArrayList<UnigramEntity>();
		try {
			for(IAnnotation<String> token : AnnotatorRegistry.getAnnotator(tokeniser).annotate(new StringAnnotation(document.toString(),0,0))){
				UnigramEntity ue = new UnigramEntity(token.getAnnotation());
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

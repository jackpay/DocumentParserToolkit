package ac.uk.susx.tag.preparser;

import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.database.UnigramEntity;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

import com.sleepycat.persist.model.Entity;

public class UnigramJob implements IJob<String>{

	public List<UnigramEntity> process(String obj) {
		ArrayList<UnigramEntity> entities = new ArrayList<UnigramEntity>();
		try {
			for(IAnnotation<String> token : StringAnnotatorEnum.TOKEN.getAnnotator().annotate(new StringAnnotation(obj,0,0))){
				UnigramEntity ue = new UnigramEntity(token.getAnnotation());
				entities.add(ue);
			}
		} catch (IncompatibleAnnotationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

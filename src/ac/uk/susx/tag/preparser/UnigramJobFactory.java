package ac.uk.susx.tag.preparser;

import ac.uk.susx.tag.database.UnigramEntity;
import ac.uk.susx.tag.document.Document;

public class UnigramJobFactory implements IJobFactory<UnigramEntity>{

	public IJob<UnigramEntity> createJob(Document doc) {
		return new UnigramJob(doc.getDocument());
	}

}

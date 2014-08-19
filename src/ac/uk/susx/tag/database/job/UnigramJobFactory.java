package ac.uk.susx.tag.database.job;

import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import ac.uk.susx.tag.document.Document;

public class UnigramJobFactory implements IJobFactory<UnigramEntity> {

	public IJob<UnigramEntity> createJob(Document doc) {
		return new UnigramJob(doc.getDocument());
	}

}

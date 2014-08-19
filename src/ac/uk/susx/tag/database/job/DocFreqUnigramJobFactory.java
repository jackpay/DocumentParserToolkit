package ac.uk.susx.tag.database.job;

import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentFreqUnigramEntity;
import ac.uk.susx.tag.document.Document;

public class DocFreqUnigramJobFactory implements IJobFactory<DocumentFreqUnigramEntity> {

	public IJob<DocumentFreqUnigramEntity> createJob(Document doc) {
		return new TokeniserJob(doc);
	}

}

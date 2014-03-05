package ac.uk.susx.tag.preparser;

import ac.uk.susx.tag.database.DocFreqUnigramEntity;
import ac.uk.susx.tag.document.Document;

public class DocFreqUnigramJobFactory implements IJobFactory<DocFreqUnigramEntity>{

	public IJob<DocFreqUnigramEntity> createJob(Document doc) {
		return new DocFreqUnigramJob(doc);
	}

}

package ac.uk.susx.tag.database.job;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentFreqUnigramEntity;
import ac.uk.susx.tag.document.Document;

public class DocFreqUnigramJobFactory implements IJobFactory<IAnnotation<String>> {

	public IJob<IAnnotation<String>> createJob(Document doc) {
		return new TokeniserJob(doc);
	}

}

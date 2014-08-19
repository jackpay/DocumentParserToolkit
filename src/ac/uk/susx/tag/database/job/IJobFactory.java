package ac.uk.susx.tag.database.job;

import ac.uk.susx.tag.database.IEntity;
import ac.uk.susx.tag.document.Document;

public interface IJobFactory<ET> {
	
	public IJob<ET> createJob(Document doc);

}

package ac.uk.susx.tag.preparser;

import ac.uk.susx.tag.database.IEntity;
import ac.uk.susx.tag.document.Document;

public interface IJobFactory<ET extends IEntity> {
	
	public IJob<ET> createJob(Document doc);

}

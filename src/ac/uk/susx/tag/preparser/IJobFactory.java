package ac.uk.susx.tag.preparser;

import ac.uk.susx.tag.database.IEntity;

public interface IJobFactory<ET extends IEntity,T> {
	
	public IJob<ET> createJob(T obj);

}

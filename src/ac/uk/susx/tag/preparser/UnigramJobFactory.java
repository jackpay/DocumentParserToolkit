package ac.uk.susx.tag.preparser;

import ac.uk.susx.tag.database.UnigramEntity;

public class UnigramJobFactory implements IJobFactory<UnigramEntity,String>{

	public IJob<UnigramEntity> createJob(String obj) {
		return new UnigramJob(obj);
	}

}

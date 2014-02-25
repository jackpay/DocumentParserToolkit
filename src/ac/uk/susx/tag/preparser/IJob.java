package ac.uk.susx.tag.preparser;

import java.util.List;

import ac.uk.susx.tag.database.UnigramEntity;

import com.sleepycat.persist.model.Entity;

public interface IJob<T>{

	public List<UnigramEntity> process(T obj);
	
}

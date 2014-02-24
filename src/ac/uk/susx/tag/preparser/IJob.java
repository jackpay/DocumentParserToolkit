package ac.uk.susx.tag.preparser;

import java.util.List;

import com.sleepycat.persist.model.Entity;

public interface IJob<T>{

	public List<Entity> process(T obj);
	
}

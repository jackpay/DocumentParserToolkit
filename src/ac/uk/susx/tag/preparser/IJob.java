package ac.uk.susx.tag.preparser;

import java.util.List;

import ac.uk.susx.tag.database.IEntity;

public interface IJob<T>{

	public List<? extends IEntity> process(T obj);

}

package ac.uk.susx.tag.preparser;

import java.util.List;

import ac.uk.susx.tag.database.IEntity;

public interface IJob<ET extends IEntity>{
	
	public List<ET> process();

}

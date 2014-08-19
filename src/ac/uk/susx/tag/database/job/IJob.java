package ac.uk.susx.tag.database.job;

import java.util.List;

public interface IJob<ET>{
	
	public List<ET> process();

}

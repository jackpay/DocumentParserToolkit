package ac.uk.susx.tag.preparser;

import java.util.List;

import com.sleepycat.persist.model.Entity;

import ac.uk.susx.tag.database.IDatabaseIndexer;
import ac.uk.susx.tag.database.IEntity;

public interface IPreParser<PE,ET,JT>{

	public IDatabaseIndexer<PE,ET> parse();

	public void index(List<ET> entities);
}

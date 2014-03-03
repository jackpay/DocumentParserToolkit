package ac.uk.susx.tag.preparser;

import java.util.List;

import com.sleepycat.persist.model.Entity;

import ac.uk.susx.tag.database.IDatabaseIndexer;

public interface IPreParser<PE,ET,JT>{

	public IDatabaseIndexer<PE,ET> parse();

	public void index(List<Entity> entities);
}

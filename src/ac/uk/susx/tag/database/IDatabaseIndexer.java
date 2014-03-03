package ac.uk.susx.tag.database;

import java.util.List;

import com.sleepycat.persist.PrimaryIndex;

public interface IDatabaseIndexer<PE,ET>{

	public PrimaryIndex<PE,ET> getPrimaryIndex();

	public DatabaseEntityStore entityStore();
	
	public void index(List<ET> entities);
	
	public void index(ET entity);

}
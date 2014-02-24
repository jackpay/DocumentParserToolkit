package ac.uk.susx.tag.database;

import com.sleepycat.persist.PrimaryIndex;

public interface IDatabaseIndexer<PE,ET>{
	
	public PrimaryIndex<PE,ET> getPrimaryIndex();
	
	public DatabaseEntityStore entityStore();

}

package ac.uk.susx.tag.database;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;

public class NGramIndexer {
	
	private DatabaseEntityStore entityStore;
	private PrimaryIndex<UnigramEntity[],NGramEntity> pIndex;
	
	public NGramIndexer() {
		entityStore = new DatabaseEntityStore();
		try {
			pIndex = entityStore.getStore().getPrimaryIndex(UnigramEntity[].class, NGramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public PrimaryIndex<UnigramEntity[],NGramEntity> getPrimaryIndex() {
		return pIndex;
	}
	
	public DatabaseEntityStore entityStore() {
		return entityStore();
	}
}

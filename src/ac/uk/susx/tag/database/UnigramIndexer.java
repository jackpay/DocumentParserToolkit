package ac.uk.susx.tag.database;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;

public class UnigramIndexer {

	private PrimaryIndex<String,UnigramEntity> pIndx;
	private DatabaseEntityStore entityStore;

	public UnigramIndexer () {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, UnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public PrimaryIndex<String,UnigramEntity> getPrimaryIndex() {
		return pIndx;
	}

}
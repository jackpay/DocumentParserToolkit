package ac.uk.susx.tag.database;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.evolve.IncompatibleClassException;

public class DatabaseEntityStore {

	private EntityStore entityStore;

	public DatabaseEntityStore() {
		StoreConfig sc = new StoreConfig();
		sc.setTransactional(true);
		sc.setAllowCreate(true);
		try {
			entityStore = new EntityStore(DatabaseEnvironment.getInstance().getEnvironment(),"EntityStore",sc);
		} catch (IncompatibleClassException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public EntityStore getStore() {
		return entityStore;
	}

	public void close() {
		if (entityStore != null) {
			try {
				entityStore.getEnvironment().close();
				entityStore.close();
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}

}

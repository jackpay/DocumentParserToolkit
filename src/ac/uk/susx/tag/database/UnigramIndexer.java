package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class UnigramIndexer implements IDatabaseIndexer<CharSequence,UnigramEntity> {

	private PrimaryIndex<CharSequence,UnigramEntity> pIndx;
	private DatabaseEntityStore entityStore;

	public UnigramIndexer () {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(CharSequence.class, UnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public PrimaryIndex<CharSequence, UnigramEntity> getPrimaryIndex() {
		return pIndx;
	}

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

	public void index(List<UnigramEntity> entities) {
		for(UnigramEntity entity : entities) {
			try {
				pIndx.put(entity);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	public void index(UnigramEntity entity) {
		try {
			pIndx.put(entity);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * No secondary index.
	 */
	public <SE> SecondaryIndex<CharSequence, SE, UnigramEntity> getSecondaryIndex() {
		return null;
	}

	@Override
	public <T> EntityIndex<?, T> getIndex(Class<T> indexType) {
		// TODO Auto-generated method stub
		return null;
	}

}
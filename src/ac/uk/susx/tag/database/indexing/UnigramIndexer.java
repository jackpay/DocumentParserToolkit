package ac.uk.susx.tag.database.indexing;

import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
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

    @Override
    public EntityIndex<CharSequence, UnigramEntity> getIndex() {
        return null;
    }

    @Override
    public void index(int id, List<CharSequence> entities) {

    }

    @Override
    public void index(int id, CharSequence entity) {

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

}
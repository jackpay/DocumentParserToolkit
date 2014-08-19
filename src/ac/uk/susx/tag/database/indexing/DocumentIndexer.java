package ac.uk.susx.tag.database.indexing;

import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentEntity;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public final class DocumentIndexer implements IDatabaseIndexer<String, DocumentEntity> {
	
	private static PrimaryIndex<String, DocumentEntity> pIndx;
	private static final DatabaseEntityStore entityStore = new DatabaseEntityStore();

	public DocumentIndexer() {
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, DocumentEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public PrimaryIndex<String, DocumentEntity> getIndex() {
		return pIndx;
	}

    @Override
    public void index(int id, List<String> entities) {

    }

    @Override
    public void index(int id, String entity) {

    }

    public static <SE> SecondaryIndex<Integer, SE, DocumentEntity> getIdIndex() {
		try {
			return (SecondaryIndex<Integer, SE, DocumentEntity>) entityStore.getStore().getSecondaryIndex(pIndx, Integer.class, "id");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

}

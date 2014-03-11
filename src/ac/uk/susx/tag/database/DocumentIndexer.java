package ac.uk.susx.tag.database;

import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class DocumentIndexer implements IDatabaseIndexer<String,DocIndexEntity> {
	
	private PrimaryIndex<String, DocIndexEntity> pIndx;
	private final DatabaseEntityStore entityStore;

	public DocumentIndexer() {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, DocIndexEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public PrimaryIndex<String, DocIndexEntity> getPrimaryIndex() {
		return pIndx;
	}

	public <SE> SecondaryIndex<String, SE, DocIndexEntity> getSecondaryIndex() {
		try {
			return (SecondaryIndex<String, SE, DocIndexEntity>) entityStore().getStore().getSecondaryIndex(getPrimaryIndex(), String.class, "docId");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

	@Override
	public void index(List<DocIndexEntity> entities) {
		for(DocIndexEntity entity : entities) {
			try {
				pIndx.put(entity);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void index(DocIndexEntity entity) {
		try {
			pIndx.put(entity);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	

}

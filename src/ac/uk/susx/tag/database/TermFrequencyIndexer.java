package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;

public class TermFrequencyIndexer implements IDatabaseIndexer<String,UnigramEntity>{
	
	private PrimaryIndex<String,UnigramEntity> pIndx;
	private final DatabaseEntityStore entityStore;

	public TermFrequencyIndexer() {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, UnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}	
	}

	public PrimaryIndex<String, UnigramEntity> getPrimaryIndex() {
		return pIndx;
	}

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

	public void index(List<UnigramEntity> entities) {
		for(UnigramEntity entity : entities) {
			UnigramEntity dbEntity = null;
			try {
				dbEntity = pIndx.get(entity.getUnigram());
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			if(dbEntity == null) {
				try {
					pIndx.put(entity);
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
			else {
				dbEntity.incrementFrequency();
			}
		}
	}

	public void index(UnigramEntity entity) {
		index(new ArrayList<UnigramEntity>(Arrays.asList(entity)));
	}

}

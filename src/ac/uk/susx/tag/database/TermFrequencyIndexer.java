package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class TermFrequencyIndexer implements IDatabaseIndexer<CharSequence,UnigramEntity>{
	
	private PrimaryIndex<CharSequence,UnigramEntity> pIndx;
	private final DatabaseEntityStore entityStore;

	public TermFrequencyIndexer() {
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
				try {
					pIndx.put(dbEntity);
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void index(UnigramEntity entity) {
		index(new ArrayList<UnigramEntity>(Arrays.asList(entity)));
	}

	/** 
	 * No secondary index.
	 */
	public <SE> SecondaryIndex<CharSequence, SE, UnigramEntity> getSecondaryIndex() {
		return null;
	}

}

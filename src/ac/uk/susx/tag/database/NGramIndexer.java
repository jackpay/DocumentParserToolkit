package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class NGramIndexer implements IDatabaseIndexer<UnigramEntity[],NGramEntity>{

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
		return entityStore;
	}

	//TODO: check for equality?!
	public void index(List<NGramEntity> entities) {
		for(NGramEntity entity : entities) {
			try {
				pIndex.put(entity);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	public void index(NGramEntity entity) {
		index(new ArrayList<NGramEntity>(Arrays.asList(entity)));
	}

	/**
	 * No secondary index.
	 */
	public <SE> SecondaryIndex<UnigramEntity[], SE, NGramEntity> getSecondaryIndex() {
		return null;
	}
}
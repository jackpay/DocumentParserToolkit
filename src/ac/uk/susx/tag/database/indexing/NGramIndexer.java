package ac.uk.susx.tag.database.indexing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.NGramEntity;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class NGramIndexer implements IDatabaseIndexer<UnigramEntity[],NGramEntity> {

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

	@Override
	public <T> EntityIndex<?, T> getIndex(Class<T> indexType) {
		// TODO Auto-generated method stub
		return null;
	}
}
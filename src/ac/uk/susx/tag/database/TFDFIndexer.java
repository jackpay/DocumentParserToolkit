package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class TFDFIndexer implements IDatabaseIndexer<String,DocFreqUnigramEntity> {
	
	private PrimaryIndex<String, DocFreqUnigramEntity> pIndx;
	private final DatabaseEntityStore entityStore;
	
	public TFDFIndexer() {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, DocFreqUnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public PrimaryIndex<String, DocFreqUnigramEntity> getPrimaryIndex() {
		return pIndx;
	}

	@SuppressWarnings("unchecked")
	public SecondaryIndex<String, String, DocFreqUnigramEntity> getSecondaryIndex() {
		try {
			return entityStore().getStore().getSecondaryIndex(getPrimaryIndex(), String.class, "docId");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

	public void index(List<DocFreqUnigramEntity> entities) {
		for(DocFreqUnigramEntity entity : entities) {
			DocFreqUnigramEntity dbEntity = null;
			try {
				dbEntity = pIndx.get(entity.getDocId());
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
				dbEntity.incrementFrequency(entity.getUnigram());
				try {
					pIndx.put(dbEntity);
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void index(DocFreqUnigramEntity entity) {
		index(Arrays.asList(entity));
	}

}

package ac.uk.susx.tag.database;

import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
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
			Transaction txn = null;
			try {
				txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
			try {
				//dbEntity = pIndx.get(entity.getDocId());
				dbEntity = pIndx.get(txn, entity.getDocId(), LockMode.RMW);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
			if(dbEntity == null) {
				try {
					pIndx.put(entity);
					txn.commit();
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
			}
			else {
				dbEntity.incrementFrequency(entity.getUnigram());
				try {
					txn.commit();
				} catch (DatabaseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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

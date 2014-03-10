package ac.uk.susx.tag.database;

import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class DocFrequencyIndexer implements IDatabaseIndexer<String,DocFreqUnigramEntity> {
	
	private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
	private PrimaryIndex<String, DocFreqUnigramEntity> pIndx;
	private final DatabaseEntityStore entityStore;
	
	public DocFrequencyIndexer() {
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
			int retry_count = 0;
			while(retry_count < MAX_DEADLOCK_RETRIES) {
				Transaction txn = null;
				try {
					txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
					DocFreqUnigramEntity dbEnt =  pIndx.get(txn, entity.getDocId(), LockMode.RMW);
					if(dbEnt == null) {
						pIndx.put(txn,entity);
						txn.commit();
					}
					else {
						dbEnt.incrementFrequency(entity.getUnigram());
						pIndx.put(txn,dbEnt);
						txn.commit();
					}
				} catch (DeadlockException e) {
					retry_count++;
					try {
						if(txn != null) {
							txn.abort();
						}
						System.err.println("AVERTED");
					} catch (DatabaseException e1) {
						System.err.println("FAILED TO AVERT");
					}
				} catch (DatabaseException e) {
					try {
						if(txn != null) {
							txn.abort();
						}
					} catch (DatabaseException e1) {
						System.err.println("FAILED TXN ABORT ON GENERIC FAILURE");
					}
					System.err.println("GENERIC SYSTEM FAILURE - ABORTING");
				}
				retry_count = MAX_DEADLOCK_RETRIES;
			}
		}
	}

	public void index(DocFreqUnigramEntity entity) {
		index(Arrays.asList(entity));
	}

}

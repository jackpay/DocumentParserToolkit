package ac.uk.susx.tag.database;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Maps;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;

public class TFDFIndexer implements IDatabaseIndexer<String,DocFreqUnigramEntity> {
	
	private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
	private PrimaryIndex<String, DocFreqUnigramEntity> pIndx;
	private PrimaryIndex<String,UnigramEntity> sIndx;
	private final DatabaseEntityStore entityStore;
	private final HashMap<String,String> failed = Maps.newHashMap();
	
	public TFDFIndexer() {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, DocFreqUnigramEntity.class);
			sIndx = entityStore.getStore().getPrimaryIndex(String.class, UnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public PrimaryIndex<String, DocFreqUnigramEntity> getTermFrequencyIndex() {
		return pIndx;
	}
	
	public PrimaryIndex<String,UnigramEntity> getCorpusFrequencyIndex() {
		return sIndx;
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
					retry_count = MAX_DEADLOCK_RETRIES;
				} catch (DeadlockException e) {
					retry_count++;
					try {
						if(txn != null) {
							txn.abort();
						}
						System.err.println("AVERTED");
						failed.put(entity.getUnigram(), entity.getDocId());
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
			}
			retry_count = 0;
			while(retry_count < MAX_DEADLOCK_RETRIES) {
				Transaction txn = null;
				try {
					txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
					UnigramEntity dbEnt =  sIndx.get(txn, entity.getUnigram(), LockMode.RMW);
					if(dbEnt == null) {
						UnigramEntity ue = new UnigramEntity(entity.getUnigram());
						ue.addDocId(entity.getDocId());
						sIndx.put(txn,ue);
						txn.commit();
					}
					else {
						dbEnt.incrementFrequency();
						dbEnt.addDocId(entity.getDocId());
						sIndx.put(txn,dbEnt);
						txn.commit();
					}
					retry_count = MAX_DEADLOCK_RETRIES;
				} catch (DeadlockException e) {
					retry_count++;
					try {
						if(txn != null) {
							txn.abort();
						}
						System.err.println("AVERTED-TF");
						failed.put(entity.getUnigram(), entity.getDocId());
					} catch (DatabaseException e1) {
						System.err.println("FAILED TO AVERT TF");
					}
				} catch (DatabaseException e) {
					try {
						if(txn != null) {
							txn.abort();
						}
					} catch (DatabaseException e1) {
						System.err.println("FAILED TXN ABORT ON GENERIC FAILURE TF");
					}
					System.err.println("GENERIC SYSTEM FAILURE - ABORTING - TF");
				}
			}
		}
	}
	
	public HashMap<String,String> getFailed() {
		return failed;
	}

	public void index(DocFreqUnigramEntity entity) {
		index(Arrays.asList(entity));
	}

	@Override
	public <T> EntityIndex<?, T> getIndex(Class<T> indexType) {
		// TODO Auto-generated method stub
		return null;
	}

}

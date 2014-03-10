package ac.uk.susx.tag.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class TermFrequencyIndexer implements IDatabaseIndexer<CharSequence,UnigramEntity>{
	
	private static final int MAX_DEADLOCK_RETRIES = 2000;
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
			int retry_count = 0;
			while(retry_count < MAX_DEADLOCK_RETRIES) {
				Transaction txn = null;
				try {
					txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
					UnigramEntity dbEnt =  pIndx.get(txn, entity.getUnigram(), LockMode.RMW);
					if(dbEnt == null) {
						pIndx.put(txn,entity);
						txn.commit();
					}
					else {
						dbEnt.incrementFrequency();
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

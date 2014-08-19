package ac.uk.susx.tag.database.indexing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class TermIndexer implements IDatabaseIndexer<CharSequence,UnigramEntity> {
	
	private static final int MAX_DEADLOCK_RETRIES = 2000;
	private PrimaryIndex<CharSequence,UnigramEntity> pIndx;
	private SecondaryIndex<String,String,UnigramEntity> sIndx;
	private final DatabaseEntityStore entityStore;

	public TermIndexer() {
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
					retry_count = MAX_DEADLOCK_RETRIES;
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
			}
		}
	}

	public void index(UnigramEntity entity) {
		index(new ArrayList<UnigramEntity>(Arrays.asList(entity)));
	}

	public SecondaryIndex<String, String, UnigramEntity> getSecondaryIndex() {
		return sIndx;
	}

	@Override
	public <T> EntityIndex<?, T> getIndex(Class<T> indexType) {
		// TODO Auto-generated method stub
		return null;
	}

}

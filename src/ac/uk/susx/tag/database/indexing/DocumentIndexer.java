package ac.uk.susx.tag.database.indexing;

import java.util.HashMap;
import java.util.List;

import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentEntity;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentFreqUnigramEntity;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import com.google.common.collect.Maps;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public final class DocumentIndexer implements IDatabaseIndexer<String, DocumentEntity> {

    private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
	private static PrimaryIndex<String, DocumentEntity> pIndx;
	private static final DatabaseEntityStore entityStore = new DatabaseEntityStore();
    private final HashMap<String,Integer> failed = Maps.newHashMap();

	public DocumentIndexer() {
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, DocumentEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	
	public PrimaryIndex<String, DocumentEntity> getIndex() {
		return pIndx;
	}

    @Override
    public void index(int id, List<String> entities) {}

    @Override
    public void index(int id, String entity) {
        int retry_count = 0;
        while(retry_count < MAX_DEADLOCK_RETRIES) {
            Transaction txn = null;
            try {
                if(getIdIndex().contains(id)) {
                    throw new DatabaseException("Item already exists in store!");
                }
                else{
                    pIndx.put(txn,new DocumentEntity(id,entity));
                }
                txn.commit();
                retry_count = MAX_DEADLOCK_RETRIES;
            } catch (DeadlockException e) {
                retry_count++;
                try {
                    if(txn != null) {
                        txn.abort();
                    }
                    System.err.println("AVERTED");
                    failed.put(entity, id);
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

    public static <SE> SecondaryIndex<Integer, SE, DocumentEntity> getIdIndex() {
		try {
			return (SecondaryIndex<Integer, SE, DocumentEntity>) entityStore.getStore().getSecondaryIndex(pIndx, Integer.class, "id");
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

}

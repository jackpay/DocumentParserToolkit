package ac.uk.susx.tag.database.indexing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.database.DatabaseEntityStore;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import com.google.common.collect.Maps;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.PrimaryIndex;

public class UnigramIndexer implements IDatabaseIndexer<IAnnotation<String>,UnigramEntity> {

    private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
	private PrimaryIndex<String,UnigramEntity> pIndx;
	private DatabaseEntityStore entityStore;
    private final HashMap<String,Integer> failed = Maps.newHashMap();

	public UnigramIndexer () {
		entityStore = new DatabaseEntityStore();
		try {
			pIndx = entityStore.getStore().getPrimaryIndex(String.class, UnigramEntity.class);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

    @Override
    public EntityIndex<String, UnigramEntity> getIndex() {
        return pIndx;
    }

    @Override
    public void index(int id, List<IAnnotation<String>> entities) {
        for(IAnnotation<String> entity : entities) {
            int retry_count = 0;
            while (retry_count < MAX_DEADLOCK_RETRIES) {
                Transaction txn = null;
                try {
                    if (pIndx.contains(txn, entity.getAnnotation(), LockMode.DEFAULT)) {
                        pIndx.get(entity.getAnnotation()).incrementFrequency();
                    } else {
                        pIndx.put(txn, new UnigramEntity(entity.getAnnotation()));
                    }
                    txn.commit();
                    retry_count = MAX_DEADLOCK_RETRIES;
                } catch (DeadlockException e) {
                    retry_count++;
                    try {
                        if (txn != null) {
                            txn.abort();
                        }
                        System.err.println("AVERTED");
                        failed.put(entity.getAnnotation(), id);
                    } catch (DatabaseException e1) {
                        System.err.println("FAILED TO AVERT");
                    }
                } catch (DatabaseException e) {
                    try {
                        if (txn != null) {
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

    @Override
    public void index(int id, IAnnotation<String> entity) {
        index(id, Arrays.asList(entity));
    }

}
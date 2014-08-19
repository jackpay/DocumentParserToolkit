package ac.uk.susx.tag.database.indexing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.database.*;
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

public class TFDFIndexer implements IDatabaseIndexer<IAnnotation<String>,DocumentFreqUnigramEntity> {
	
	private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
    private PrimaryIndex<Integer,DocumentFreqUnigramEntity> pIndex;
	private UnigramIndexer uIndx;
	private final DatabaseEntityStore entityStore;
	private final HashMap<String,Integer> failed = Maps.newHashMap();
	
	public TFDFIndexer() {
		entityStore = new DatabaseEntityStore();
        uIndx = new UnigramIndexer();
        try {
            pIndex = entityStore.getStore().getPrimaryIndex(Integer.class, DocumentFreqUnigramEntity.class);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

    @Override
    public void index(int id, List<IAnnotation<String>> entities) {
        DocumentEntity de = null;
        try {
            de = DocumentIndexer.getIdIndex().get(id); // Get the document entity for this id.
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        for(IAnnotation<String> entity : entities) {
            int retry_count = 0;
            while(retry_count < MAX_DEADLOCK_RETRIES) {
                Transaction txn = null;
                try {
                    txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
                    DocumentFreqUnigramEntity dfue;
                    if(pIndex.contains(txn,id,LockMode.DEFAULT)) {
                        dfue = pIndex.get(txn,id,LockMode.DEFAULT);
                    }
                    else {
                        dfue = new DocumentFreqUnigramEntity(de);
                    }
                    UnigramEntity ue = null;
                    if(uIndx.getIndex().contains(txn,entity.getAnnotation(),LockMode.DEFAULT)) {
                        ue = uIndx.getIndex().get(txn,entity.getAnnotation(),LockMode.DEFAULT);
                        ue.incrementFrequency();
                    }
                    else {
                        ue = new UnigramEntity(entity.getAnnotation());
                    }
                    dfue.incrementFrequency(ue);
                    txn.commit();
                    retry_count = MAX_DEADLOCK_RETRIES;
                } catch (DeadlockException e) {
                    retry_count++;
                    try {
                        if(txn != null) {
                            txn.abort();
                        }
                        System.err.println("AVERTED");
                        failed.put(entity.getAnnotation(), id);
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

    @Override
    public void index(int id, IAnnotation<String> entity) {
        index(id, Arrays.asList(entity));
    }

    public HashMap<String,Integer> getFailed() {
		return failed;
	}

    public UnigramIndexer getUnigramIndexer() {
        return uIndx;
    }

    @Override
    public EntityIndex<Integer, DocumentFreqUnigramEntity> getIndex() {
        return pIndex;
    }

}

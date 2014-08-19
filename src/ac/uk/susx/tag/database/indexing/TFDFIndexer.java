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
import com.sleepycat.persist.SecondaryIndex;

public class TFDFIndexer implements IDatabaseIndexer<String,DocumentFreqUnigramEntity> {
	
	private static final int MAX_DEADLOCK_RETRIES = 2000; // Arbitrary but large retry limit.
    private PrimaryIndex<DocumentEntity,DocumentFreqUnigramEntity> pIndex;
	private UnigramIndexer uIndx;
	private final DatabaseEntityStore entityStore;
	private final HashMap<String,Integer> failed = Maps.newHashMap();
	
	public TFDFIndexer() {
		entityStore = new DatabaseEntityStore();
        uIndx = new UnigramIndexer();
        try {
            pIndex = entityStore.getStore().getPrimaryIndex(DocumentEntity.class, DocumentFreqUnigramEntity.class);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

	public DatabaseEntityStore entityStore() {
		return entityStore;
	}

    public void index(List<String> entities) {

	}


//	public PrimaryIndex<String, DocumentFreqUnigramEntity> getDocFrequencyIndex() {
//		return pIndx;
//	}
//
//    public PrimaryIndex<String, UnigramEntity> getUnigramFrequency() { return uniIndx; }
//
//    public PrimaryIndex<String, DocumentEntity> getDocumentFrequency() { return docIndex; }

    @Override
    public void index(int id, List<String> entities) {
        DocumentEntity de = null;
        try {
            de = DocumentIndexer.getIdIndex().get(id); // Get the document entity for this id.
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        for(String entity : entities) {
            int retry_count = 0;
            while(retry_count < MAX_DEADLOCK_RETRIES) {
                Transaction txn = null;
                try {
                    txn = entityStore.getStore().getEnvironment().beginTransaction(null, null);
                    UnigramEntity ue = uIndx.getIndex().get(entity);
                    if(ue == null) {
                        uIndx.index(id,entity);
                        pIndex.put(new DocumentFreqUnigramEntity(de,ue));
                        txn.commit();
                    }
                    else {
                        pIndex.pu
                        ue.incrementFrequency();
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
                    e.printStackTrace();
                }
            }
        }
    }

    public HashMap<String,String> getFailed() {
		return failed;
	}

    @Override
    public EntityIndex<String, DocumentFreqUnigramEntity> getIndex() {
        return pIndex;
    }

}

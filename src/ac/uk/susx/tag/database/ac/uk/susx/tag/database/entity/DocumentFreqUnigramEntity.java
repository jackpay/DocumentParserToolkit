package ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity;

import ac.uk.susx.tag.database.IEntity;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import java.util.HashMap;

@Entity
public class DocumentFreqUnigramEntity implements IEntity {
	
	@PrimaryKey
	private int docId;
    private HashMap<String,Integer> tokens;
    private int numTokens;

    public DocumentFreqUnigramEntity() {}

	public DocumentFreqUnigramEntity(DocumentEntity docId, String token) {
        this(docId);
        tokens = new HashMap<String,Integer>();
        tokens.put(token,1);
        numTokens = 1;
	}

    public DocumentFreqUnigramEntity(DocumentEntity docId) {
        this.docId = docId.getId();
        tokens = new HashMap<String,Integer>();;
        numTokens = 0;
    }
    
    public int getNumTokens() {
    	return numTokens;
    }
	
	public int getDocId() {
		return docId;
	}
	
	public int getFrequency(String entity) {
		return tokens.get(entity);
	}
	
	public void incrementFrequency(String entity) {
        numTokens++;
        if(tokens.get(entity) != null){
            tokens.put(entity,tokens.get(entity)+1);
        }
        else{
            tokens.put(entity,1);
            numTokens++;
        }
	}
}

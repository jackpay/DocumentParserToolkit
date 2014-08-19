package ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity;

import ac.uk.susx.tag.database.IEntity;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import java.util.ArrayList;

@Entity
public class DocumentFreqUnigramEntity implements IEntity {
	
	@PrimaryKey
	private int docId;
    private ArrayList<UnigramEntity> tokens;
    private int numTokens;

    public DocumentFreqUnigramEntity() {}

	public DocumentFreqUnigramEntity(DocumentEntity docId, UnigramEntity token) {
        this(docId);
        tokens.add(token);
        numTokens = 1;
	}

    public DocumentFreqUnigramEntity(DocumentEntity docId) {
        this.docId = docId.getId();
        tokens = new ArrayList<UnigramEntity>();
        numTokens = 0;
    }
	
	public int getDocId() {
		return docId;
	}
	
	public int getFrequency(UnigramEntity entity) {
		return tokens.get(tokens.indexOf(entity)).getFrequency();
	}
	
	public void incrementFrequency(UnigramEntity entity) {
        numTokens++;
        if(tokens.contains(entity)){
            tokens.get(tokens.indexOf(entity)).incrementFrequency();
            tokens.add(entity);
        }
        else{
            entity.incrementFrequency();
            tokens.add(entity);
        }
	}
	
}

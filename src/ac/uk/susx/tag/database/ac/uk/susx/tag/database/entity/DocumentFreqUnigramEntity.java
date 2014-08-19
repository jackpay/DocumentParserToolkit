package ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity;

import ac.uk.susx.tag.database.IEntity;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class DocumentFreqUnigramEntity implements IEntity {
	
	@PrimaryKey
	private final DocumentEntity docId;
    @SecondaryKey(relate= Relationship.MANY_TO_MANY)
    private final UnigramEntity token;
    private int freq;
	
	public DocumentFreqUnigramEntity(DocumentEntity docId, UnigramEntity token) {
		this.docId = docId;
		this.token = token;
        this.freq = 1;
	}
	
	public UnigramEntity getUnigram() {
		return token;
	}
	
	public DocumentEntity getDocId() {
		return docId;
	}
	
	public int getFrequency(String token) {
		return freq;
	}
	
	public void incrementFrequency() {
        freq++;
	}
	
}

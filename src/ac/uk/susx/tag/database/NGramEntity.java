package ac.uk.susx.tag.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class NGramEntity {
	
	@PrimaryKey
	private final UnigramEntity[] ngram;
	private int frequency;
	
	public NGramEntity(int size) {
		ngram = new UnigramEntity[size];
		frequency = 0;
	}
	
	public NGramEntity(UnigramEntity[] ngram) {
		this.ngram = ngram;
		frequency = 0;
	}
	
	public UnigramEntity[] getNGram() {
		return ngram;
	}
	
	public int getFreq() {
		return frequency;
	}
}

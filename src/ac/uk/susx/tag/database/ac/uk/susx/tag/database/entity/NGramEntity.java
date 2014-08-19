package ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity;

import ac.uk.susx.tag.database.IEntity;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class NGramEntity implements IEntity {

	@PrimaryKey
	private UnigramEntity[] ngram;
	private int frequency;

    public NGramEntity() {}

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
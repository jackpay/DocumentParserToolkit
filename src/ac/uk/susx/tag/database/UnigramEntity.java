package ac.uk.susx.tag.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class UnigramEntity {

	@PrimaryKey
	private final String unigrm;
	private int freq;

	public UnigramEntity(String uni) {
		this.unigrm = uni;
		freq = 0;
	}

	public String getUnigram() {
		return unigrm;
	}

	public int getFrequency() {
		return freq;
	}

}
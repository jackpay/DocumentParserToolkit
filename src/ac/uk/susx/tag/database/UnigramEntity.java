package ac.uk.susx.tag.database;


import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class UnigramEntity implements IEntity {

	@PrimaryKey
	private String unigrm;
	private int freq;
	
	public UnigramEntity() {}
	
	public UnigramEntity(String uni) {
		this.unigrm = uni;
		freq = 1;
	}
	
	public void setUnigram(String unigrm) {
		this.unigrm = unigrm;
	}

	public String getUnigram() {
		return unigrm;
	}

	public int getFrequency() {
		return freq;
	}
	
	public void incrementFrequency() {
		freq++;
	}

}
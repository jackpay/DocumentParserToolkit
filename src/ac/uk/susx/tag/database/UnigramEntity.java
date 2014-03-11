package ac.uk.susx.tag.database;


import java.util.HashSet;

import com.beust.jcommander.internal.Sets;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class UnigramEntity implements IEntity {

	@PrimaryKey
	private String unigrm;
	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private int freq;
	private final HashSet<String> docsAppearing;
	
	public UnigramEntity() {
		docsAppearing = new HashSet<String>();
	}
	
	public UnigramEntity(String uni) {
		this();
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
	
	public HashSet<String> getAppearingDocIds() {
		return docsAppearing;
	}
	
	public void incrementFrequency() {
		freq++;
	}
	
	public void addDocId(String docId) {
		docsAppearing.add(docId);
	}

}
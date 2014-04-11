package ac.uk.susx.tag.database;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class DocFreqUnigramEntity implements IEntity {
	
	@PrimaryKey
	private String docId;
	private String token;
	private HashMap<String,Integer> freq;
	
	public DocFreqUnigramEntity(String token, String id) {
		this.docId = id;
		this.freq = Maps.newHashMap();
		this.token = token;
		freq.put(token, 1);
	}
	
	public DocFreqUnigramEntity() {}
	
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
	public String getUnigram() {
		return token;
	}
	
	public String getDocId() {
		return docId;
	}
	
	public int getFrequency(String token) {
		if(freq.get(token) == null){
			return 0;
		}
		return freq.get(token);
	}
	
	public void incrementFrequency(String token) {
		if(freq.get(token) == null){
			freq.put(token, 1);
		}
		else {
			int f = freq.get(token).intValue() + 1;
			freq.put(token,f);
		}
	}
	
}

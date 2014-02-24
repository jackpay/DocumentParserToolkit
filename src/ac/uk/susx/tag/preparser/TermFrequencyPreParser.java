package ac.uk.susx.tag.preparser;

import java.io.File;
import java.util.List;

import com.sleepycat.persist.model.Entity;

import ac.uk.susx.tag.database.IDatabaseIndexer;
import ac.uk.susx.tag.database.UnigramEntity;
import ac.uk.susx.tag.database.UnigramIndexer;

public class TermFrequencyPreParser implements IPreParser<String,UnigramEntity,String>{
	
	private List<File> files;
	
	public TermFrequencyPreParser(List<File> files) {
		this.files = files;
	}

	public IDatabaseIndexer<String,UnigramEntity> parse() {
		UnigramIndexer ui = new UnigramIndexer();
		
		return null;
	}
	
	public IJob<String> createJob(String sentence) {
		return new ;
		
	}

	public void index(List<Entity> entities) {
		// TODO Auto-generated method stub
		
	}

}

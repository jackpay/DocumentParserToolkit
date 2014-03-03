package ac.uk.susx.tag.preparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import ac.uk.susx.tag.database.IDatabaseIndexer;
import ac.uk.susx.tag.database.UnigramEntity;
import ac.uk.susx.tag.database.UnigramIndexer;

public class TermFrequencyPreParser implements IPreParser<String,UnigramEntity,String>{

	private final List<File> files;
	private final UnigramIndexer indexer;

	public TermFrequencyPreParser(List<File> files) {
		this.files = files;
		this.indexer = new UnigramIndexer();
	}

	public IDatabaseIndexer<String,UnigramEntity> parse() {
		for(File file : files) {
			String fileStr = null;
			try {
				fileStr = FileUtils.readFileToString(file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}

	public IJob<String> createJob(String sentence) {
		return new UnigramJob();

	}

	public void index(List<UnigramEntity> entities) {
		
	}
	
	public void index(UnigramEntity entity) {
		index(new ArrayList<UnigramEntity>(Arrays.asList(entity)));
	}

}

package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.database.IDatabaseIndexer;
import ac.uk.susx.tag.preparser.IJobFactory;

public class ConcurrentLinePreProcessor<PE,ET> implements IProcessor {
	
	private final IDatabaseIndexer<PE,ET> indexer;
	private final IJobFactory<ET> jobFactory;

	public ConcurrentLinePreProcessor(IDatabaseIndexer<PE,ET> indexer, IJobFactory<ET> jobFactory) { 
		this.indexer = indexer;
		this.jobFactory = jobFactory;
	}
	
	public void processFiles(List<File> files) {
		
	}

	public void processFile(File file) {
		processFiles(new ArrayList<File>(Arrays.asList(file)));
	}

}

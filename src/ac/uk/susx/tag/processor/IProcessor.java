package ac.uk.susx.tag.processor;

import java.io.File;

public interface IProcessor {
	
	public void processFiles(String fileDir);
	
	public void processFile(File file);

}

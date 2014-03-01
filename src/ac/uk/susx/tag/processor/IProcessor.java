package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.List;

public interface IProcessor {
	
	public void processFiles(List<File> files);
	
	public void processFile(File file);

}

package ac.uk.susx.tag.processor;

import java.io.File;
import java.util.List;

public interface IProcessor <DT,AT>{
	
	public void processFiles(List<File> files);
	
	public void processFile(File file);

}

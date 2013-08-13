package ac.uk.susx.tag.parser;

import java.io.File;
import java.util.List;

public interface Parser <DT,AT>{
	
	public void parseFiles(List<File> files);
	
	public void parseFile(File file);

}

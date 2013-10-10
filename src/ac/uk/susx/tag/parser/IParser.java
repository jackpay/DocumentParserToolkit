package ac.uk.susx.tag.parser;

import java.io.IOException;

public interface IParser <DT,AT> {

	public void init(String[] args);
	
	public boolean parse() throws IOException;

}

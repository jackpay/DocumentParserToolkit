package ac.uk.susx.tag.parser;

import java.io.IOException;

public interface Parser <DT,AT> {

	public void init(String[] args);
	
	public boolean parse() throws IOException;

}

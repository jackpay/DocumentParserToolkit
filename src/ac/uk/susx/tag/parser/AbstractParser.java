package ac.uk.susx.tag.parser;

import java.io.IOException;

public abstract class AbstractParser <AT,DT> {
	
	public abstract void init(String[] args);
	
	public abstract boolean parse() throws IOException;

}

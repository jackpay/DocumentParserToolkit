package ac.uk.susx.tag.parser;

import java.io.IOException;

public abstract class AbstractParser <AT,DT> {
	
	static {
		loadClasses();
	}
	
	public abstract void init(String[] args);
	
	public abstract boolean parse() throws IOException;

	private static void loadClasses() { }
}

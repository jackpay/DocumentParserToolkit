package ac.uk.susx.tag.processor;

import java.io.IOException;

public interface Processor <DT,AT> {

	public void init(String[] args);
	
	public boolean process() throws IOException;

}

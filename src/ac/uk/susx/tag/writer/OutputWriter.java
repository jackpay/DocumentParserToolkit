package ac.uk.susx.tag.writer;

import java.io.IOException;

public interface OutputWriter<A>{
	
	public void init(String fileName) throws IOException;
	
	public void writeToken(A token) throws IOException;
	
	public void closeDocument() throws IOException;
	
	public boolean isDocumentOpen();
	
}

package ac.uk.susx.tag.writer;

import java.io.IOException;

public interface IOutputWriter {
	
	public void init(String fileName) throws IOException;
	
	public void writeToken(CharSequence token) throws IOException;
	
	public void closeDocument() throws IOException;
	
	public boolean isDocumentOpen();
	
}

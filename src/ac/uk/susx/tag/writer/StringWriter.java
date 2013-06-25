package ac.uk.susx.tag.writer;

import java.io.IOException;

public class StringWriter extends AbstractOutputWriter<String>{
	
	public StringWriter(String fileName) throws IOException{
		init(fileName);
	}

	public void writeToken(String token) throws IOException {
		getWriter().write(token);
	}

}

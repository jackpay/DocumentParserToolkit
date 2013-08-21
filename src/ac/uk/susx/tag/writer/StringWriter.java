package ac.uk.susx.tag.writer;

import java.io.IOException;

public class StringWriter extends AbstractOutputWriter<String>{
	
	public StringWriter(String fileName) throws IOException{
		init(fileName);
	}

	public synchronized void writeToken(String seq) {
		getWriter().print(seq);
	}

}

package ac.uk.susx.tag.writer;

import java.io.IOException;

public class CharSequenceWriter extends AbstractOutputWriter<CharSequence>{
	
	public CharSequenceWriter(String fileName) throws IOException{
		init(fileName);
	}

	public synchronized void writeToken(CharSequence seq) {
		getWriter().print(seq);
	}

}

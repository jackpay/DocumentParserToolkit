package ac.uk.susx.tag.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractOutputWriter <A> implements OutputWriter<A>{
	
	private File file;
	private BufferedWriter writer;

	public void init(String fileName) throws IOException {
		file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
				file.setWritable(true);
				try {
					writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			throw new IOException("File already " + fileName + " exists");
		}
	}

	public void closeDocument() throws IOException {
		writer.close();
	}

	public boolean isDocumentOpen() {
		return file.canWrite();
	}
	
	public BufferedWriter getWriter(){
		return writer;
	}

}

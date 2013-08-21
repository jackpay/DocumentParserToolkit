package ac.uk.susx.tag.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractOutputWriter <A> implements OutputWriter<A>{
	
	private File file;
	private PrintWriter writer;

	public void init(String fileName) throws IOException {
		file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
				file.setWritable(true);
				try {
					writer = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile())));
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
			throw new IOException("File: " + fileName + ". Already exists");
		}
	}

	public void closeDocument() throws IOException {
		writer.close();
	}

	public boolean isDocumentOpen() {
		return file.canWrite() && writer != null;
	}
	
	protected PrintWriter getWriter(){
		return writer;
	}

}

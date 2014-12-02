package ac.uk.susx.tag.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StandardOutputWriter implements IOutputWriter {
	
	private File file;
	private PrintWriter writer;
	
	public StandardOutputWriter(String fileName) {
		try {
			init(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(String fileName) throws IOException {
		file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
				file.setWritable(true);
				try {
					writer = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile())));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
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
	
	public synchronized void writeToken(CharSequence seq) {
		getWriter().print(seq);
	}

}

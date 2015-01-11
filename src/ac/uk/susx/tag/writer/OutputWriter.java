package ac.uk.susx.tag.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter extends BufferedWriter {
	
	public OutputWriter(String fileName) throws IOException {
		super(new FileWriter(new File(fileName),true));
	}
	
	public OutputWriter(File file) throws IOException {
		super(new FileWriter(file,true));
	}

}

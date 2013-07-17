package ac.uk.susx.tag.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringInputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.utils.ParserUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 *
 */
public class StringDocumentProcessor extends AbstractConcurrentDocumentProcessor <String,String> implements Processor <String,String>{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringDocumentProcessor gdp = new StringDocumentProcessor();
		gdp.init(args);
		try {
			gdp.process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(String[] args) {
		GrammaticalInputParser gip = new GrammaticalInputParser();
		setConfiguration(gip.parseInputParameters(args));
		OutputDocumentFormatter<String> outputWriter = new StringOutputDocumentFormatter();
		getConfig().setOutputWriter(outputWriter);
		getConfig().setDocumentBuilder(new StringInputDocumentFormatter());
	}

	public boolean process() throws IOException {
		if(getConfig() == null){
			throw new IOException("Configuration object file not initialised. Must process input parameters.");
		}
		if(getConfig().getOutputWriter() == null){
			throw new IOException("Output writer not initialised.");
		}
		if(getConfig().getDocumentBuilder() == null){
			throw new IOException("Document builder not initialised.");
		}
		
		ArrayList<File> files = ParserUtils.getFiles(getConfig().getInputLocation(), getConfig().getInputSuff());
		parseFiles(files);
	
		return true;
	}
}

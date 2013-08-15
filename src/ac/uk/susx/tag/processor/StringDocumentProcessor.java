package ac.uk.susx.tag.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringInputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringBagOfWordsOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.parser.ConcurrentStringSentenceParser;
import ac.uk.susx.tag.utils.ParserUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 */
public class StringDocumentProcessor implements Processor<String,String> {
	
	private ConcurrentStringSentenceParser parser;
	private Configuration<Document<String,String>,String,String> config;

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
		config = gip.parseInputParameters(args);
		OutputDocumentFormatter<String,String> outputWriter = new StringBagOfWordsOutputDocumentFormatter();
		config.setOutputWriter(outputWriter);
		config.setDocumentBuilder(new StringInputDocumentFormatter());
		parser = new ConcurrentStringSentenceParser(config);
	}

	public boolean process() throws IOException {
		
		if(config == null){
			throw new IOException("Configuration object file not initialised. Must process input parameters.");
		}
		if(config.getOutputWriter() == null){
			throw new IOException("Output writer not initialised.");
		}
		if(config.getDocumentBuilder() == null){
			throw new IOException("Document builder not initialised.");
		}
		if(parser == null){
			throw new IOException("Parser not initialised.");
		}
		
		ArrayList<File> files = ParserUtils.getFiles(config.getInputLocation(), config.getInputSuff());
		parser.parseFiles(files);
	
		return true;
	}
}

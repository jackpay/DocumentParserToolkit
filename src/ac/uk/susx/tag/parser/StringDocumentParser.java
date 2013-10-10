package ac.uk.susx.tag.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.filter.ExcludeAnnotationFilter;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.filter.IncludeAnnotationFilter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringInputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringBagOfWordsOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.processor.ConcurrentDocumentProcessor;
import ac.uk.susx.tag.processor.ConcurrentStringLineProcessor;
import ac.uk.susx.tag.processor.ConcurrentStringLineProcessor;
import ac.uk.susx.tag.processor.ConcurrentStringSentenceProcessor;
import ac.uk.susx.tag.utils.FileUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 */
public class StringDocumentParser implements IParser<String,String> {
	
	private ConcurrentStringLineProcessor parser;
	private IConfiguration<IDocument<String,String>,String,String> config;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringDocumentParser gdp = new StringDocumentParser();
		gdp.init(args);
		try {
			gdp.parse();
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
		ArrayList<String> anns = new ArrayList<String>();
		anns.add("DT");
		anns.add("CC");
		anns.add("CD");
		config.addFilter(new ExcludeAnnotationFilter<String>(anns, StringAnnotatorEnum.POSTAG.getAnnotator().getClass(), true));
		parser = new ConcurrentStringLineProcessor(config);
	}

	public boolean parse() throws IOException {
		
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
		
		ArrayList<File> files = FileUtils.getFiles(config.getInputLocation(), config.getInputSuff());
		parser.processFiles(files);
	
		return true;
	}
}

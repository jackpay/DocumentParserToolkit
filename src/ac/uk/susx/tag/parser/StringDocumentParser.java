package ac.uk.susx.tag.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.sleepycat.je.DatabaseException;

import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.database.TermFrequencyIndexer;
import ac.uk.susx.tag.database.UnigramEntity;
import ac.uk.susx.tag.filter.RemoveAnnotationFilter;
import ac.uk.susx.tag.formatting.IOutputDocumentFormatter;
import ac.uk.susx.tag.formatting.BasicInputDocumentFormatter;
import ac.uk.susx.tag.formatting.BagOfWordsOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.preparser.UnigramJobFactory;
import ac.uk.susx.tag.processor.ConcurrentLinePreProcessor;
import ac.uk.susx.tag.processor.ConcurrentLineProcessor;
import ac.uk.susx.tag.utils.FileUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 */
public class StringDocumentParser extends AbstractParser<String,String> {
	
	private ConcurrentLineProcessor parser;
	private ConcurrentLinePreProcessor<String,UnigramEntity> preparser;
	private IConfiguration<CharSequence> config;
	private TermFrequencyIndexer indexer;

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
		IOutputDocumentFormatter<CharSequence> outputWriter = new BagOfWordsOutputDocumentFormatter();
		config.setOutputWriter(outputWriter);
		config.setDocumentBuilder(new BasicInputDocumentFormatter());
		ArrayList<String> anns = new ArrayList<String>();
		anns.add("DT");
		anns.add("CC");
		anns.add("CD");
		config.addFilter(new RemoveAnnotationFilter<String>(anns, PoSTagAnnotator.class, false));
		parser = new ConcurrentLineProcessor(config);
		indexer = new TermFrequencyIndexer();
		preparser = new ConcurrentLinePreProcessor<String,UnigramEntity>(indexer, new UnigramJobFactory());
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
		preparser.processFiles(files);
		parser.processFiles(files);
		try {
			System.err.println(indexer.getPrimaryIndex().get("the").getFrequency());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return true;
	}
}

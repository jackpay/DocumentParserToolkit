package ac.uk.susx.tag.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.configuration.GrammaticalConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.formatting.StringOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.ParserUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 *
 */
public class GrammaticalDocumentProcessor implements Processor {
	
	GrammaticalConfiguration config;
	StringOutputDocumentFormatter outputWriter;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GrammaticalDocumentProcessor gdp = new GrammaticalDocumentProcessor();
		gdp.init(args);
		try {
			gdp.process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(String[] args) {
		GrammaticalInputParser gip = new GrammaticalInputParser();
		config = gip.parseInputParams(args);
		outputWriter = new StringOutputDocumentFormatter();
	}

	public boolean process() throws IOException {
		if(config == null){
			throw new IOException("Configuration object file not initialised. Must process input parameters!");
		}
		if(outputWriter == null){
			throw new IOException("Output writer not initialised!");
		}
		
		ArrayList<File> files = ParserUtils.getFiles(config.getInputLocation(), config.getInputSuff());
		String outputSuff = config.getOutputSuff() == null ?  "" : config.getOutputSuff();
		
		for(File file : files){
			for(Annotator<Document<String,String>,?,String> annotator : config.getAnnotators()){
				StringDocument doc = new StringDocument(file.getAbsolutePath());
				try {
					annotator.annotate(doc);
					System.err.println("Processing file: " + file.getAbsolutePath());
					outputWriter.processOutput(doc, config.getOutputLocation() + "/" + file.getName() + outputSuff, GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator().getClass());
				} catch (IncompatibleAnnotationException e) {
					e.printStackTrace();
					return false;
				}
			}
			System.err.println("Processing finished successfully!");
		}
		
		return true;
	}
}

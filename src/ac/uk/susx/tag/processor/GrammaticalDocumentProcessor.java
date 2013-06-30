package ac.uk.susx.tag.processor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.configuration.Configuration;
import ac.uk.susx.tag.configuration.GrammaticalConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringInputDocumentFormatter;
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
		config = gip.parseInputParameters(args);
		OutputDocumentFormatter<String> outputWriter = new StringOutputDocumentFormatter();
		config.setOutputWriter(outputWriter);
	}

	public boolean process() throws IOException {
		if(config == null){
			throw new IOException("Configuration object file not initialised. Must process input parameters!");
		}
		if(config.getOutputWriter() == null){
			throw new IOException("Output writer not initialised!");
		}
		
		ArrayList<File> files = ParserUtils.getFiles(config.getInputLocation(), config.getInputSuff());
		long startTime = System.currentTimeMillis();
		ConcurrentDocumentProcessor<String,String> cdp = new ConcurrentDocumentProcessor<String,String>(files, new StringInputDocumentFormatter(), config);
		long endTime = System.currentTimeMillis();
		System.err.println(endTime-startTime);
//		String outputSuff = config.getOutputSuff() == null ?  "" : config.getOutputSuff();
//		
//		for(File file : files){
//			Document<String,String> doc = new StringDocument(ParserUtils.readFileAsString(file));
//			System.err.println("Processing file: " + file.getAbsolutePath());
//			for(Annotator<Document<String,String>,?,String,String> annotator : config.getAnnotators()){
//				try {
//					annotator.annotate(doc);
//				} catch (IncompatibleAnnotationException e) {
//					e.printStackTrace();
//					return false;
//				}
//			}
//			doc.retainAnnotations(config.getOutputIncludedAnnotators()); // Create subset of annotations to be present in the output.
//			config.getOutputWriter().processOutput(doc, config.getOutputLocation() + "/" + file.getName() + outputSuff, GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator().getClass());
//		}
		//System.err.println("Processing finished successfully!");
		return true;
	}
}

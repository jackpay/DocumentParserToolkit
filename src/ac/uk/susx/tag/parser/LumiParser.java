package ac.uk.susx.tag.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.filter.LowercaseFilter;
import ac.uk.susx.tag.filter.RegexFilter;
import ac.uk.susx.tag.filter.RemoveAnnotationFilter;
import ac.uk.susx.tag.filter.RetainAnnotationFilter;
import ac.uk.susx.tag.filter.StopWordFilter;
import ac.uk.susx.tag.formatting.LumiMsgPackDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.formatting.StringBagOfWordsOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.processor.ConcurrentDocumentProcessor;
import ac.uk.susx.tag.processor.ConcurrentStringLineProcessor;
import ac.uk.susx.tag.utils.FileUtils;

public class LumiParser implements IParser<String,String>{

	
	private ConcurrentStringLineProcessor parser;
	private IConfiguration<IDocument<String,String>,String,String> config;
	private ArrayList<File> files;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LumiParser gdp = new LumiParser();
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
		config.setDocumentBuilder(new LumiMsgPackDocumentFormatter());
//		ArrayList<String> anns = new ArrayList<String>();
//		anns.add("DT");
//		anns.add("CC");
//		anns.add("CD");
//		anns.add("IN");
//		anns.add("{");
//		anns.add("}");
//		anns.add("[");
//		anns.add("]");
//		anns.add(")");
//		anns.add("(");
//		//config.addFilter(new RemoveAnnotationFilter<String>(anns, StringAnnotatorEnum.POSTAG.getAnnotator().getClass(), true));
		config.addFilter(new RegexFilter("[^A-Za-z0-9 ]",StringAnnotatorEnum.TOKEN.getAnnotator().getClass()));
		config.addFilter(new StopWordFilter(StringAnnotatorEnum.TOKEN.getAnnotator().getClass()));
		config.addFilter(new RegexFilter("[^A-Za-z0-9 ]",StringAnnotatorEnum.STEMMER.getAnnotator().getClass()));
		config.addFilter(new StopWordFilter(StringAnnotatorEnum.STEMMER.getAnnotator().getClass()));
		config.addFilter(new RegexFilter("[^A-Za-z0-9 ]",StringAnnotatorEnum.LEMMATISER.getAnnotator().getClass()));
		config.addFilter(new StopWordFilter(StringAnnotatorEnum.LEMMATISER.getAnnotator().getClass()));
		config.addFilter(new RemoveAnnotationFilter<String>(new ArrayList<String>(Arrays.asList("-lrb-","-rrb-")),StringAnnotatorEnum.LEMMATISER.getAnnotator().getClass(),true));
		//config.addFilter(new RetainAnnotationFilter<String>(new ArrayList<String>(Arrays.asList("NN","NNS","NNP","NNPS")), StringAnnotatorEnum.POSTAG.getAnnotator().getClass(),true));
		config.addFilter(new RetainAnnotationFilter<String>(new ArrayList<String>(Arrays.asList("NN","NNS","NNP","NNPS","VB","VBD","VBG","VBN","VBP","VBZ")), StringAnnotatorEnum.POSTAG.getAnnotator().getClass(),true));
		//config.addFilter(new RetainAnnotationFilter<String>(new ArrayList<String>(Arrays.asList("NN","NNS","NNP","NNPS","JJ","JJR","JJS")), StringAnnotatorEnum.POSTAG.getAnnotator().getClass(),true));
		//config.addFilter(new RetainAnnotationFilter<String>(new ArrayList<String>(Arrays.asList("NN","NNS","NNP","NNPS","VB","VBD","VBG","VBN","VBP","VBZ","JJ","JJR","JJS")), StringAnnotatorEnum.POSTAG.getAnnotator().getClass(),true));
		config.addFilter(new LowercaseFilter(StringAnnotatorEnum.TOKEN.getAnnotator().getClass()));
		parser = new ConcurrentStringLineProcessor(config);
		try {
			files = FileUtils.getFiles(config.getInputLocation(), config.getInputSuff());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		
		parser.processFiles(files);
	
		return true;
	}
}

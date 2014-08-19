package ac.uk.susx.tag.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentFreqUnigramEntity;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.FrequencyAnnotator;
import ac.uk.susx.tag.annotator.FrequencyAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.database.DatabaseEnvironment;
import ac.uk.susx.tag.database.indexing.TFDFIndexer;
import ac.uk.susx.tag.filter.RemoveAnnotationFilter;
import ac.uk.susx.tag.formatting.IOutputDocumentFormatter;
import ac.uk.susx.tag.formatting.BasicInputDocumentFormatter;
import ac.uk.susx.tag.formatting.BagOfWordsOutputDocumentFormatter;
import ac.uk.susx.tag.input.GrammaticalInputParser;
import ac.uk.susx.tag.database.job.DocFreqUnigramJobFactory;
import ac.uk.susx.tag.processor.ConcurrentLinePreProcessor;
import ac.uk.susx.tag.processor.ConcurrentLineProcessor;
import ac.uk.susx.tag.processor.IProcessor;
import ac.uk.susx.tag.utils.FileUtils;

/**
 * The main calling class for using the parsing system. Acts as a container for all of the main objects of the system.
 * @author jp242
 */
public class StringDocumentParser extends AbstractParser<String,String> {
	
	private ConcurrentLineProcessor parser;
	private ConcurrentLinePreProcessor<String, DocumentFreqUnigramEntity> preparser;
	private IConfiguration<CharSequence> config;
	private TFDFIndexer indexer;

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
		indexer = new TFDFIndexer();
		preparser = new ConcurrentLinePreProcessor<String,DocumentFreqUnigramEntity>(indexer, new DocFreqUnigramJobFactory());
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
		
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.submit(new ProcessorCallable(preparser,files));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		System.err.println("Finished pre-processing");
		FrequencyAnnotator anno = null;
		try {
			anno = (FrequencyAnnotator) AnnotatorRegistry.getAnnotator(FrequencyAnnotatorFactory.class);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		es = Executors.newSingleThreadExecutor();
		es.submit(new ProcessorCallable(parser, files));
		es.shutdown();
		try {
			es.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		System.err.println("Finished all parsing.");
		
		try {
			System.err.println(indexer.getTermFrequencyIndex().get("3").getFrequency("overload"));
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		try {
			System.err.println(preparser.getDocumentIndex().getPrimaryIndex().get("3").getDocName());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		for(String token : indexer.getFailed().keySet()) {
			try {
				System.err.println("AVERTED token: " + token + " id: " + indexer.getFailed().get(token) + " freq: " + indexer.getTermFrequencyIndex().get(indexer.getFailed().get(token)).getFrequency(token));
				System.err.println(preparser.getDocumentIndex().getPrimaryIndex().get(indexer.getFailed().get(token)).getDocName());
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		int freq = 0;
		try {
			EntityCursor<DocumentFreqUnigramEntity> ec = indexer.getTermFrequencyIndex().entities();
			for(DocumentFreqUnigramEntity entity : ec){
				freq += entity.getFrequency("and");
			}
			ec.close();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println("SumFreq: " + freq + " Freq: " + indexer.getCorpusFrequencyIndex().get("and").getFrequency());
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		preparser.getDocumentIndex().entityStore().close();
		indexer.entityStore().close();
		DatabaseEnvironment.getInstance().close();
		
		return true;
	}
	
	private class ProcessorCallable implements Callable<Void> {
		
		private final IProcessor processor;
		private final List<File> files;
		
		public ProcessorCallable(IProcessor processor, List<File> files) {
			this.processor = processor;
			this.files = files;
		}

		@Override
		public Void call() throws Exception {
			processor.processFiles(files);
			return null;
		}
		
	}
}

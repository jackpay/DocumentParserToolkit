package ac.uk.susx.tag.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentEntity;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.DocumentFreqUnigramEntity;
import ac.uk.susx.tag.database.ac.uk.susx.tag.database.entity.UnigramEntity;
import ac.uk.susx.tag.formatting.HTMLStripperDocumentFormatter;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;

import ac.uk.susx.tag.annotator.PoSTagAnnotator;
import ac.uk.susx.tag.annotator.FrequencyAnnotator;
import ac.uk.susx.tag.annotator.FrequencyAnnotatorFactory;
import ac.uk.susx.tag.annotator.TokenAnnotator;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.configuration.IConfiguration;
import ac.uk.susx.tag.database.DatabaseEnvironment;
import ac.uk.susx.tag.database.indexing.TFDFIndexer;
import ac.uk.susx.tag.filter.CanonicaliseFilter;
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
	private ConcurrentLinePreProcessor<IAnnotation<String>, DocumentFreqUnigramEntity> preparser;
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
		config.setDocumentBuilder(new HTMLStripperDocumentFormatter());
		ArrayList<String> anns = new ArrayList<String>();
		anns.add("DT");
		anns.add("CC");
		anns.add("CD");
		anns.add("NNP");
		anns.add("JJ");
		String repl = "POOOOOOOPOOOOOO";
		config.addFilter(new CanonicaliseFilter<String>(repl,"DICTIONARY",TokenAnnotator.class));
		config.addFilter(new RemoveAnnotationFilter<String>(anns, PoSTagAnnotator.class, true));
		parser = new ConcurrentLineProcessor(config);
//		indexer = new TFDFIndexer();
//		preparser = new ConcurrentLinePreProcessor<IAnnotation<String>,DocumentFreqUnigramEntity>(indexer, new DocFreqUnigramJobFactory());
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
		System.err.println("Finished all parsing.");
		
//        System.err.println("Started pre-processing");
//		ExecutorService es = Executors.newSingleThreadExecutor();
//        ProcessorCallable pc = new ProcessorCallable(preparser,files);
//		es.submit(pc);
//		try {
//			es.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
//		} catch (InterruptedException e2) {
//			e2.printStackTrace();
//		}
////        try {
////            int freq = 0;
////            EntityCursor<UnigramEntity> ec = indexer.getUnigramIndexer().getIndex().entities();
////            for(UnigramEntity entity : ec){
////                System.err.println(entity.getUnigram() + " " + entity.getFrequency());
////                freq += entity.getFrequency();
////            }
////            ec.close();
////        } catch (DatabaseException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//		System.err.println("Finished pre-processing");
//		FrequencyAnnotator anno = null;
//		try {
//			anno = (FrequencyAnnotator) AnnotatorRegistry.getAnnotator(FrequencyAnnotatorFactory.class);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		ExecutorService pr = Executors.newSingleThreadExecutor();
//		pr.submit(new ProcessorCallable(parser, files));
//		pr.shutdown();
//		try {
//			pr.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
//		} catch (InterruptedException e2) {
//			e2.printStackTrace();
//		}
//		try {
//			System.err.println(preparser.getDocumentIndex().getIdIndex().get(3));
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//		}
//		
//		for(String token : indexer.getFailed().keySet()) {
//			try {
//				System.err.println("AVERTED token: " + token + " id: " + indexer.getFailed().get(token) + " freq: " + indexer.getFailed().get(token));
//				System.err.println(preparser.getDocumentIndex().getIdIndex().get(indexer.getFailed().get(token)).name());
//			} catch (DatabaseException e) {
//				e.printStackTrace();
//			}
//		}
//		int freq = 0;
//		try {
//			EntityCursor<UnigramEntity> ec = indexer.getUnigramIndexer().getIndex().entities();
//			for(UnigramEntity entity : ec){
//                System.err.println(entity.getUnigram() + " " + entity.getFrequency());
//				freq += entity.getFrequency();
//			}
//			ec.close();
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			System.out.println("SumFreq: " + freq + " Freq: " + indexer.getIndex().g.get("and").getFrequency());
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		preparser.getDocumentIndex().entityStore().close();
//		indexer.entityStore().close();
        //indexer.getUnigramIndexer().entityStore().close();
//		DatabaseEnvironment.getInstance().close();
		
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

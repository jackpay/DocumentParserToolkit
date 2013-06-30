package ac.uk.susx.tag.input;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import ac.uk.susx.tag.configuration.GrammaticalConfiguration;

public class GrammaticalInputParser extends AbstractInputParameterParser {
	
	public class GrammaticalInputReader extends AbstractInputReader {
		
		@Parameter
		(names = {"-pos", "--posTag"}, description="Pos tag annotations")
		private boolean postag = false;
		
		@Parameter
		(names = {"-ct", "chunkToken"}, description="Chunk token annotations")
		private boolean chunkToken = false;
		
		@Parameter
		(names = {"-s", "--sentence"}, description="Sentence annotations")
		private boolean sentenceTag = false;
		
		@Parameter 
		(names = {"-t", "--token"}, description="Token annotations")
		private boolean token = true;
		
		public boolean chunkToken(){
			return chunkToken;
		}
		
		public boolean token(){
			return token;
		}
		
		public boolean sentence(){
			return sentenceTag;
		}
		
		public boolean posTag(){
			return postag;
		}
		
	}

	@Override
	public GrammaticalConfiguration parseInputParameters(String[] args) {
		GrammaticalInputReader reader = new GrammaticalInputReader();
		JCommander jcomm = new JCommander(reader, args);
		GrammaticalConfiguration gc = new GrammaticalConfiguration(reader.input(), reader.output());

		gc.setInputSuff(reader.suffix());
		gc.setOutSuff(reader.outSuffix());
		gc.setSingleFileOutput(reader.singleFileOutput());
		
		if(reader.token()){
			gc.addAnnotator(GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator(),true);
		}
		if(reader.sentence()){
			gc.addAnnotator(GrammaticalConfiguration.AnnotatorTypes.SENTENCE.getAnnotator(), true);
		}
		if(reader.posTag()){
			gc.addAnnotator(GrammaticalConfiguration.AnnotatorTypes.POSTAG.getAnnotator(), true);
		}
		if(reader.chunkToken()){
			gc.addAnnotator(GrammaticalConfiguration.AnnotatorTypes.CHUNKTAG.getAnnotator(), true);
		}
		
		return gc;
	}

}

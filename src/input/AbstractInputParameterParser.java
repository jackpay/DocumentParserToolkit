package input;

import com.beust.jcommander.Parameter;

import configuration.AbstractConfig;

/**
 * Abstract class for parsing input parameters with a few basic inputs implemented.
 * @author jackpay
 *
 */
public abstract class AbstractInputParameterParser implements InputParameterParser{
	
	public abstract class AbstractInputReader {
		
		@Parameter
		(names = {"-i", "--inputLoc"}, description="Input location",
		required = true)
		private String inputLocation = null;
		
		@Parameter
		(names = {"-o", "--outputDir"}, description="Output directory",
		required = true)
		private String outputLocation = null;
		
		@Parameter
		(names = {"-suff", "--inputFileSuffix"}, description="Input file Suffix",
		required = true)
		private String suff = null;
		
		@Parameter
		(names = {"-pos --posTag"}, description="Pos tag annotations")
		private boolean postag = false;
		
		@Parameter
		(names = {"-cs", "--chunkSpan"}, description="Chunk span annotations")
		private boolean chunkSpan = false;
		
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
		
		public boolean chunkSpan(){
			return chunkSpan;
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
		
		public String input(){
			return inputLocation;
		}
		
		public String output(){
			return outputLocation;
		}
		
		public String suffix(){
			return suff;
		}
	}

	public abstract AbstractConfig parseInputParams();

}

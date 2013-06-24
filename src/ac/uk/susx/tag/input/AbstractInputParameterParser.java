package ac.uk.susx.tag.input;

import ac.uk.susx.tag.configuration.Configuration;

import com.beust.jcommander.Parameter;


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
		(names = {"-osuff", "--outputFileSuffix"}, description="Output file suffix")
		private String osuff = null;
		
		@Parameter
		(names = {"-sf", "--singleFileOutput"}, description="Set to true if output is required in one file.")
		private boolean singleFile = false;
		
		public String input(){
			return inputLocation;
		}
		
		public String output(){
			return outputLocation;
		}
		
		public String suffix(){
			return suff;
		}
		
		public String outSuffix(){
			return osuff;
		}
		
		public boolean singleFileOutput(){
			return singleFile;
		}
	}
	
	public abstract Configuration<?,?> parseInputParams(String[] args);

}

package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

public class TokenAnnotatorFactory implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-t";
	private static final String DESC = "A tokeniser which splits a given text into the individual tokens.";

	public IAnnotator<String,String> create() {
		return new TokenAnnotator();
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}

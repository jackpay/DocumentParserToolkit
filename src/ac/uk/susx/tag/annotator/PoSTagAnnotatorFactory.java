package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class PoSTagAnnotatorFactory implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-pos";
	private static final String DESC = "A pos tag annotator, which annotates each token with its part of speech.";

	public IAnnotator<String,String> create() {
		return new PoSTagAnnotator(TokenAnnotatorFactory.class);
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}

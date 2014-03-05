package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class TermFrequencyAnnotatorFactory implements IAnnotatorFactory<String,String> {
	
	private static final String CMD = "-tf";
	private static final String DESC = "Annotates terms with their corpus wide frequency";

	public IAnnotator<String, String> create() {
		return new TermFrequencyAnnotator(TokenAnnotatorFactory.class);
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}

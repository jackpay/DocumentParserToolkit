package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class ContextWindowAnnotatorFactory implements IAnnotatorFactory<String,String>{
	
	private static final String CMND = "-con";
	private static final String DESC = "Annotates token with a context window surrounding them.";

	@Override
	public IAnnotator<String, String> create() {
		return new ContextWindowAnnotator(5);
	}

	@Override
	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMND,DESC);
	}

}

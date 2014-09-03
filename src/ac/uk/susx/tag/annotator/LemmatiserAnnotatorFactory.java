package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class LemmatiserAnnotatorFactory implements IAnnotatorFactory<String,String> {
	
	private static final String CMD = "-lem";
	private static final String DESC = "Reduces tokens to their smallest lemma.";

	public IAnnotator<String, String> create() {
		return new LemmatiserAnnotator(TokenAnnotatorFactory.class);
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

	@Override
	public IAnnotator<String, String> create(String[] params) {
		// TODO Auto-generated method stub
		return create();
	}

}

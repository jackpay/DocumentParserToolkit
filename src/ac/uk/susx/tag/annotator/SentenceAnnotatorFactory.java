package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class SentenceAnnotatorFactory implements IAnnotatorFactory<Sentence,String> {
	
	private static final String CMD = "-sent";
	private static final String DESC = "A sentence annotator, which splits a given document by its discovered sentence bourndaries.";

	public IAnnotator<Sentence,String> create() {
		return new SentenceAnnotator();
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}	
	
	
	@Override
	public IAnnotator<Sentence, String> create(String[] params) {
		// TODO Auto-generated method stub
		return create();
	}

	@Override
	public String name() {
		return "sentence";
	}

}

package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class ChunkTagAnnotatorFactory implements IAnnotatorFactory<String,String> {
	
	private static final String CMD = "-ct";
	private static final String DESC = "A chunk tag annotator, providing each token with a tag representing the grammatical chunk for which it is a member.";

	public IAnnotator<String,String> create() {
		return new ChunkTagAnnotator(TokenAnnotatorFactory.class, PoSTagAnnotatorFactory.class);
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}

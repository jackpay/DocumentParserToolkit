package ac.uk.susx.tag.annotator;

import uk.ac.susx.tag.dependencyparser.datastructures.Token;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.CommandLineOption;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.utils.IllegalInputParamsException;

@AnnotatorFactory
public class DependencyAnnotatorFactory implements IAnnotatorFactory<Token,String> {
	
	private static final String CMD = "-dep";
	private static final String DESC = "Annotate tokens with their dependency relations";

	@Override
	public IAnnotator<Token, String> create() {
		return new DependencyAnnotator(TokenAnnotatorFactory.class,PoSTagAnnotatorFactory.class);
	}

	@Override
	public IAnnotator<Token, String> create(String[] params) throws IllegalInputParamsException {
		return create();
	}

	@Override
	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "dependency-parser";
	}
	

}

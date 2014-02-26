package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class PersonAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-per";
	private static final String DESC = "A person annotator, tagging a collection of token(s) if they appear to be describing a person.";
	
	public PersonAnnotatorFactory(){
		super("nerperson.bin",TokenAnnotatorFactory.class);
	}

	public IAnnotator<String,String> create() {
		return new PersonAnnotatorFactory();
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}

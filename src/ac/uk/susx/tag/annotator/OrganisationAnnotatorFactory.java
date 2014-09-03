	package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class OrganisationAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-org";
	private static final String DESC = "An organisation annotator, tagging a collection of token(s) if they appear to be describing an organisation.";

	public OrganisationAnnotatorFactory() {
		super("nerorganization.bin", TokenAnnotatorFactory.class);
	}

	public IAnnotator<String, String> create() {
		return new OrganisationAnnotatorFactory();
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

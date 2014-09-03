package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class LocationAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-loc";
	private static final String DESC = "A location annotator, tagging a collection of token(s) if they appear to be describing a location.";

	public LocationAnnotatorFactory(){
		super("nerlocation.bin",TokenAnnotatorFactory.class);
	}

	public IAnnotator<String, String> create() {
		return new LocationAnnotatorFactory();
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

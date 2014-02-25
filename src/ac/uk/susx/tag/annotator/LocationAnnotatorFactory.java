package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

public class LocationAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String,String>{
	
	private static final String CMD = "-loc";

	public LocationAnnotatorFactory(){
		super("nerlocation.bin",TokenAnnotatorFactory.class);
	}

	public IAnnotator<String, String, String> create() {
		return new LocationAnnotatorFactory();
	}

	public String getCommandLineOption() {
		return CMD;
	}
	
	
}

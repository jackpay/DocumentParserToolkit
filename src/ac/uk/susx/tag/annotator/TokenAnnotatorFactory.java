package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

public class TokenAnnotatorFactory implements IAnnotatorFactory<String,String,String>{
	
	private static final String CMD = "-t";

	public IAnnotator<String, String, String> create() {
		return new TokenAnnotator();
	}

	public String getCommandLineOption() {
		return CMD;
	}

}

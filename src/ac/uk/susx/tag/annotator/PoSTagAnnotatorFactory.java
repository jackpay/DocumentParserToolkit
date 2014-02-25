package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

@AnnotatorFactory
public class PoSTagAnnotatorFactory implements IAnnotatorFactory<String,String,String>{
	
	private static final String CMD = "-pos";

	public IAnnotator<String, String, String> create() {
		return new PoSTagAnnotator(TokenAnnotatorFactory.class);
	}

	public String getCommandLineOption() {
		return CMD;
	}

}

package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

@AnnotatorFactory
public class SentenceAnnotatorFactory implements IAnnotatorFactory<String,String,String> {
	
	private static final String CMD = "-sent";

	public IAnnotator<String,String,String> create() {
		return new SentenceAnnotator();
	}

	public String getCommandLineOption() {
		// TODO Auto-generated method stub
		return null;
	}

}

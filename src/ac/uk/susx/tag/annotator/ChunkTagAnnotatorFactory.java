package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

public class ChunkTagAnnotatorFactory implements IAnnotatorFactory<String,String,String> {
	
	private static final String CMD = "-ct";

	public IAnnotator<String, String, String> create() {
		return new ChunkTagAnnotator(TokenAnnotatorFactory.class, PoSTagAnnotatorFactory.class);
	}

	public String getCommandLineOption() {
		return CMD;
	}

}

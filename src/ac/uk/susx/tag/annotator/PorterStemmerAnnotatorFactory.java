package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.input.CommandLineOption;

@AnnotatorFactory
public class PorterStemmerAnnotatorFactory implements IAnnotatorFactory<String,String> {
	
	private static final String CMD = "-stem";
	private static final String DESC = "Stems tokens to their nearest lemma using a basic PorterStemmer implementation."; 

	public IAnnotator<String, String> create() {
		return new PorterStemmerAnnotator(TokenAnnotatorFactory.class);
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

}
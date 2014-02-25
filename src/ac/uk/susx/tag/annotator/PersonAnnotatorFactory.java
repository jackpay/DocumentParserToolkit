package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

@AnnotatorFactory
public class PersonAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String,String>{
	
	private static final String CMD = "-per";
	
	public PersonAnnotatorFactory(){
		super("nerperson.bin",TokenAnnotatorFactory.class);
	}

	public IAnnotator<String, String, String> create() {
		return new PersonAnnotatorFactory();
	}

	public String getCommandLineOption() {
		return CMD;
	}

}

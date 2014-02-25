	package ac.uk.susx.tag.annotator;

import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

@AnnotatorFactory
public class OrganisationAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String,String>{
	
	public static final String CMD = "-org";

	public OrganisationAnnotatorFactory() {
		super("nerorganization.bin", TokenAnnotatorFactory.class);
	}

	public IAnnotator<String, String, String> create() {
		return new OrganisationAnnotatorFactory();
	}

	public String getCommandLineOption() {
		return CMD;
	}

}

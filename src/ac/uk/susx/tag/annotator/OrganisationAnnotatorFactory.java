	package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.CommandLineOption;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

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

	@Override
	public String name() {
		return "organisation";
	}
}

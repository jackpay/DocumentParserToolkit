package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.input.CommandLineOption;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

@AnnotatorFactory
public class PersonAnnotatorFactory extends AbstractNERAnnotator implements IAnnotatorFactory<String,String>{
	
	private static final String CMD = "-per";
	private static final String DESC = "A person annotator, tagging a collection of token(s) if they appear to be describing a person.";
	
	public PersonAnnotatorFactory(){
		super("nerperson.bin",TokenAnnotatorFactory.class);
	}

	public IAnnotator<String,String> create() {
		return new PersonAnnotatorFactory();
	}

	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMD,DESC);
	}

	@Override
	public IAnnotator<String, String> create(String[] params) {
		return create();
	}

	@Override
	public List<? extends IAnnotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<? extends IAnnotation<String>> tokens = null;
		try {
			tokens = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) getTokeniser());
			if(tokens == null) {
				tokens = AnnotatorRegistry.getAnnotator(getTokeniser()).annotate(sentence);
			}
		} catch (IllegalAnnotationStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//List<IAnnotation<String>> annos = annotate(sentence.getSentence());
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);
		List<IAnnotation<String>> annos = findNames(strToks,sentence.getSentence());
		sentence.addAnnotations(this.getClass(), annos);
		return annos;
	}

	@Override
	public String name() {
		return "person";
	}

}

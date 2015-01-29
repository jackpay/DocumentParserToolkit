package ac.uk.susx.tag.annotator;

import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.factory.AnnotatorFactory;
import ac.uk.susx.tag.annotator.factory.CommandLineOption;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.indexing.OffsetIndexToken;
import ac.uk.susx.tag.utils.IllegalInputParamsException;

@AnnotatorFactory
public class ContextWindowAnnotatorFactory implements IAnnotatorFactory<List<Annotation<?>>,String>{
	
	private static final String CMND = "-con";
	private static final String DESC = "Annotates token with a context window surrounding them.";
	private static final int DEFAULT_WINDOW = 5;

	@Override
	public IAnnotator<List<Annotation<?>>, String> create() {
		return new ContextWindowAnnotator(DEFAULT_WINDOW);
	}

	@Override
	public CommandLineOption getCommandLineOption() {
		return new CommandLineOption(CMND,DESC);
	}

	@Override
	public IAnnotator<List<Annotation<?>>, String> create(String[] params) throws IllegalInputParamsException {
		int window = 0;
		try{
			window = Integer.parseInt(params[0]);
			return new ContextWindowAnnotator(window);
		}
		catch(Exception e) {
			try{
				return new ContextWindowAnnotator(DEFAULT_WINDOW);
			}
			catch(Exception e1) {
				throw new IllegalInputParamsException(this.getClass());
			}
		}
	}

	@Override
	public String name() {
		return "context-window";
	}

}

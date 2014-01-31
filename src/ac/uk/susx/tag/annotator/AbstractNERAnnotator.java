package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.registry.AnnotatorEnum;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.AnnotationUtils;

public abstract class AbstractNERAnnotator extends AbstractAnnotator<String,String,String> {
	
	private NameFinderME nameFinder;
	private static final String TOKDELIM = "-";
	private final String modelName;
	
	public AbstractNERAnnotator(String modelName){
		this.modelName = modelName;
	}

	public synchronized List<IAnnotation<String>> annotate(IAnnotation<String> sentence) throws IncompatibleAnnotationException {
		startModel(); // Ensure model is live.
		ArrayList<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		Collection<? extends IAnnotation<String>> tokens = AnnotatorEnum.TOKEN.getAnnotator().annotate(sentence);
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);

		Span[] peopleSpans = nameFinder.find(strToks);
		
		for(Span span : peopleSpans){
			StringAnnotation annotation = new StringAnnotation(buildAnnotation(Arrays.copyOfRange(strToks, span.getStart(), span.getEnd()),span.getType()), sentence.getStart() + span.getStart(), sentence.getStart() + span.getEnd());
			annotations.add(annotation);
		}
		return annotations;
	}
	
	private String buildAnnotation(String[] tokens, String annotationType){
		StringBuilder sb = new StringBuilder();
		for(String token : tokens){
			sb.append(token);
			sb.append(TOKDELIM);
		}
		sb.append(annotationType);
		return sb.toString();
	}

	public void startModel() {
		if(!modelStarted()){
			try {
				nameFinder = new NameFinderME(new TokenNameFinderModel(this.getClass().getClassLoader().getResourceAsStream(modelName)));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean modelStarted() {
		return nameFinder != null;
	}

}

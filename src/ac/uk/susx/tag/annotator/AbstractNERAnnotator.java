package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.AnnotationUtils;

public abstract class AbstractNERAnnotator extends AbstractStringAnnotator{
	
	private NameFinderME nameFinder;
	private static final String TOKDELIM = "-";
	private final String modelName;
	
	public AbstractNERAnnotator(String modelName){
		this.modelName = modelName;
	}

	public synchronized Map<IndexToken, StringAnnotation> annotate(Annotation<String> sentence)
			throws IncompatibleAnnotationException {
		startModel(); // Ensure model is live.
		HashMap<IndexToken, StringAnnotation> annotations = new HashMap<IndexToken, StringAnnotation>();
		Map<IndexToken, ? extends Annotation<String>> tokens = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(sentence);
		String[] strToks = AnnotationUtils.annotationsToArray(AnnotationUtils.annotationsToSortedArrayList(tokens), new String[tokens.size()]);

		Span[] peopleSpans = nameFinder.find(strToks);
		
		for(Span span : peopleSpans){
			StringAnnotation annotation = new StringAnnotation(buildAnnotation(Arrays.copyOfRange(strToks, span.getStart(), span.getEnd()),span.getType()), sentence.getStart() + span.getStart(), sentence.getStart() + span.getEnd());
			annotations.put(annotation.getOffset(), annotation);
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

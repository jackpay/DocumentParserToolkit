package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.AnnotationUtils;

public abstract class AbstractNERAnnotator extends AbstractAnnotator<String,String> {
	
	private NameFinderME nameFinder;
	private static final String TOKDELIM = "-";
	private final String modelName;
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	
	public AbstractNERAnnotator(String modelName, Class<? extends IAnnotatorFactory<String,String>> tokeniser){
		this.modelName = modelName;
		this.tokeniser = tokeniser;
	}

	public synchronized List<Annotation<String>> annotate(Annotation<String> sentence) throws IncompatibleAnnotationException {

		List<? extends Annotation<String>> tokens = null;
		try {
			tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);

		return findNames(strToks,sentence);
	}
	
	protected List<Annotation<String>> findNames(String[] tokens,Annotation<String> sentence) {
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		Span[] peopleSpans = nameFinder.find(tokens);
		
		if(peopleSpans == null || peopleSpans.length == 0) {
			return annotations;
		}
		
		for(Span span : peopleSpans){
			StringAnnotation annotation = new StringAnnotation(buildAnnotation(Arrays.copyOfRange(tokens, span.getStart(), span.getEnd()),span.getType()), sentence.getStart() + span.getStart(), sentence.getStart() + span.getEnd());
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
	
	@Override
	public List<? extends Annotation<String>> annotate(final Sentence sentence) throws IncompatibleAnnotationException {
		List<? extends Annotation<String>> tokens = null;
		try {
			tokens = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(tokeniser).getClass());
			if(tokens == null || tokens.isEmpty()) {
				tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
			}
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);
		List<Annotation<String>> annos = findNames(strToks,sentence.getSentence());
		sentence.addAnnotations(this.getClass(), annos);
		return annos;
	}

	public boolean modelStarted() {
		return nameFinder != null;
	}

}

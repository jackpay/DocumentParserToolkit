package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;
import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * An annotator which can annotate a document using its raw text from the Document object
 * or annotate a pre-tokenised collection.
 * @author jackpay
 *
 */
public final class PoSTagAnnotator extends AbstractAnnotator<String,String> {
	
	private POSTaggerME postagger;
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	
	public PoSTagAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser) {
		this.tokeniser = tokeniser;
	}

	/**
	 * Annotates a single un-tokenised sentence.
	 * @throws IncompatibleAnnotationException 
	 */
	public synchronized List<IAnnotation<String>> annotate (IAnnotation<String> sentence) throws IncompatibleAnnotationException {
		ArrayList<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		List<? extends IAnnotation<String>> tokens = null;
		try {
			tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] strToks = AnnotationUtils.annotationsToArray(tokens, new String[tokens.size()]);
		String[] strTags = postagger.tag(strToks);
		int begin = 0;
		for(int i = 0; i < strTags.length; i++){
			Pattern pattern = Pattern.compile(Pattern.quote(strToks[i]));
			Matcher matcher = pattern.matcher(sentence.getAnnotation());
			matcher.find(begin);
			StringAnnotation annotation = new StringAnnotation(strTags[i], (sentence.getStart() + matcher.start()), (sentence.getStart() + matcher.end()));
			annotations.add(annotation);
			begin = matcher.end();
		}
		return annotations;
	}
	
	public void startModel() {
		if(!modelStarted()){
			try {
				postagger = new POSTaggerME(new POSModel(this.getClass().getClassLoader().getResourceAsStream("enposmaxent.bin")));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean modelStarted() {
		return postagger != null;
	}

	public List<? extends IAnnotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<IAnnotation<String>> postagged = annotate(sentence.getSentence());
		sentence.addAnnotations(this.getClass(), postagged);
		return postagged;
	}
	
}
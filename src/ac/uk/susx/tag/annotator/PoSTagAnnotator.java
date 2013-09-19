package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;

import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * An annotator which can annotate a document using its raw text from the Document object
 * or annotate a pre-tokenised collection.
 * @author jackpay
 *
 */
public final class PoSTagAnnotator extends AbstractStringAnnotator {
	
	private POSTaggerME postagger;

	/**
	 * Annotates a single un-tokenised sentence.
	 * @throws IncompatibleAnnotationException 
	 */
	public synchronized Collection<StringAnnotation> annotate (
			Annotation<String> sentence) throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		Collection<? extends Annotation<String>> tokens = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(sentence);
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
}
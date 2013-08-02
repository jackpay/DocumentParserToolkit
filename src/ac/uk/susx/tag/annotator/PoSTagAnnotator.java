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

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.ParserUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

/**
 * An annotator which can annotate a document using its raw text from the Document object
 * or annotate a pre-tokenised collection.
 * @author jackpay
 *
 */
public final class PoSTagAnnotator implements Annotator<Document<String,String>, StringAnnotation, String, String>{
	
	private POSTaggerME postagger;

	public void annotate(Document<String,String> doc) throws IncompatibleAnnotationException {
		annotate(doc, true);
	}

	/**
	 * Annotates a document with postag annotations
	 * @throws IncompatibleAnnotationException 
	 */
	public void annotate(Document<String,String> doc, boolean parseRawText) throws IncompatibleAnnotationException {
			Collection<Annotation<String>> postags = new ArrayList<Annotation<String>>();
			Collection<? extends Annotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.OFFSET_SENTENCE.getAnnotator().getClass());
			if(sentences == null){
				StringAnnotatorEnum.OFFSET_SENTENCE.getAnnotator().annotate(doc);
			}
			sentences = doc.getAnnotations(StringAnnotatorEnum.OFFSET_SENTENCE.getAnnotator().getClass()); 
			postags.addAll(annotate(sentences));
			doc.addAnnotations(this.getClass(), postags);
	}

	/**
	 * Takes pre-tokenised sentence and annotates it.
	 * @throws IncompatibleAnnotationException 
	 */
	public Collection<StringAnnotation> annotate(
			Collection<? extends Annotation<String>> sentence) throws IncompatibleAnnotationException {
		
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		for(Annotation<String> sentence : sentences){
			annotations.addAll(annotate(sentence));
		}
		return annotations;
	}

	/**
	 * Annotates a single un-tokenised sentence.
	 * @throws IncompatibleAnnotationException 
	 */
	public synchronized Collection<StringAnnotation> annotate (
			Annotation<String> sentence) throws IncompatibleAnnotationException {
		
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		Collection<? extends Annotation<String>> tokens = StringAnnotatorEnum.STORED_TOKEN.getAnnotator().annotate(sentence);
		String[] strToks = ParserUtils.annotationsToArray(tokens, new String[tokens.size()]);
		String[] strTags = postagger.tag(strToks);
		int begin = 0;
		for(int i = 0; i < strTags.length; i++){
			Pattern pattern = Pattern.compile(Pattern.quote(strToks[i]));
			Matcher matcher = pattern.matcher(sentence.getAnnotation());
			matcher.find(begin);
			StringAnnotation annotation = new StringAnnotation(strTags[i], sentence.getStart()+matcher.start(), sentence.getStart()+matcher.end());
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

package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.GrammaticalAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.ParserUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public final class TokenAnnotator implements Annotator<Document<String,String>, GrammaticalAnnotation, String>{
	
	private TokenizerME tokeniser;

	public void annotate(Document<String,String> doc) throws IncompatibleAnnotationException{
		annotate(doc, true);
	}

	public void annotate(Document<String,String> doc, boolean parseRawText) throws IncompatibleAnnotationException{
		GrammaticalAnnotation docAnn = new GrammaticalAnnotation(doc.getDocument(),0,doc.getDocument().length());
		Collection<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		annotations.addAll(annotate(docAnn));
		doc.addAnnotations(this.getClass(), annotations);
	}
	
	public Collection<GrammaticalAnnotation> annotate(Collection<? extends Annotation<String>> annotations) 
																throws IncompatibleAnnotationException{
		String[] annotationStrings = ParserUtils.annotationsToArray(annotations, new String[annotations.size()]);
		ArrayList<GrammaticalAnnotation> tokens = new ArrayList<GrammaticalAnnotation>();
		int length = 0;
		for(String string : annotationStrings){
			GrammaticalAnnotation ga = new GrammaticalAnnotation(string, length, length+string.length());
			length = string.length();
			tokens.addAll(annotate(ga));
		}
		return tokens;
	}

	public Collection<GrammaticalAnnotation> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException{
		
		ArrayList<GrammaticalAnnotation> annotations = new ArrayList<GrammaticalAnnotation>();
		String docStr = annotation.getAnnotation();
		String[] tokens = tokeniser.tokenize(docStr);
		int begin = 0;
		for(int i = 0; i < tokens.length; i++){
			Pattern pattern = Pattern.compile(Pattern.quote(tokens[i]));
			Matcher matcher = pattern.matcher(docStr);
			matcher.find(begin);
			GrammaticalAnnotation ann = new GrammaticalAnnotation(tokens[i], annotation.getStart()+matcher.start(), annotation.getStart()+matcher.end());
			annotations.add(ann);
			begin = matcher.end();
		}
		return annotations;
	}
	
	public boolean modelStarted() {
		return tokeniser != null;
	}
	
	public void startModel() {
		if(!modelStarted()){
			try {
				tokeniser = new TokenizerME(new TokenizerModel(getClass().getClassLoader().getResourceAsStream("entoken.bin")));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

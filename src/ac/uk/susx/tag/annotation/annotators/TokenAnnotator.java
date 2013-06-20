package ac.uk.susx.tag.annotation.annotators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import ac.uk.susx.tag.annotation.annotations.GrammaticalAnnotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;
import ac.uk.susx.tag.annotation.annotator.AnnotatorUtils;
import ac.uk.susx.tag.document.StringDocument;

public class TokenAnnotator implements Annotator<StringDocument, GrammaticalAnnotation>{
	
	private TokenizerME tokeniser;

	public void annotate(StringDocument doc) {
		annotate(doc, true);
	}

	public void annotate(StringDocument doc, boolean parseRawText) {
		
		String docStr = doc.getDocument();
		ArrayList<GrammaticalAnnotation> annotations = createAnnotations(docStr);
		doc.addAnnotations(this.getClass(), annotations);
	}
	
	public ArrayList<GrammaticalAnnotation> annotate(Collection<GrammaticalAnnotation> annotations) {
		String[] annotationStrings = AnnotatorUtils.annotationsToArray(annotations);
		ArrayList<GrammaticalAnnotation> tokens = new ArrayList<GrammaticalAnnotation>();
		for(String string : annotationStrings){
			tokens.addAll(createAnnotations(string));
		}
		return tokens;
	}
	
	public ArrayList<GrammaticalAnnotation> createAnnotations(String docStr){
		String[] tokens = tokeniser.tokenize(docStr);
		ArrayList<GrammaticalAnnotation> annotations = new ArrayList<GrammaticalAnnotation>();
		int begin = 0;
		Pattern tok;
		Matcher matcher;
		for(int i = 0; i < tokens.length; i++){
			tok = Pattern.compile(tokens[i]);
			matcher = tok.matcher(docStr);
			matcher.find(begin);
			GrammaticalAnnotation annotation = new GrammaticalAnnotation(tokens[i], tokens[i], i, matcher.start(), matcher.end());			
			annotations.add(annotation);
			begin = matcher.end();
		}
		return annotations;
	}

	public boolean modelStarted() {
		return tokeniser == null;
	}
	
	public void startModel() {
		if(tokeniser == null){
			try {
				tokeniser = new TokenizerME(new TokenizerModel(this.getClass().getClassLoader().getResourceAsStream("/entoken.bin")));
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.ParserUtils;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public final class TokenAnnotator implements Annotator<Document<String,String>, StringAnnotation, String, String>{
	
	private TokenizerME tokeniser;

	public void annotate(Document<String,String> doc) throws IncompatibleAnnotationException{
		annotate(doc, true);
	}

	public void annotate(Document<String,String> doc, boolean parseRawText) throws IncompatibleAnnotationException{
		StringAnnotation docAnn = new StringAnnotation(doc.getDocument(),0,doc.getDocument().length());
		Collection<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		annotations.addAll(annotate(docAnn));
		doc.addAnnotations(this.getClass(), annotations);
	}
	
	public Collection<StringAnnotation> annotate(Collection<? extends Annotation<String>> annotations) 
																throws IncompatibleAnnotationException{
		String[] annotationStrings = ParserUtils.annotationsToArray(annotations, new String[annotations.size()]);
		ArrayList<StringAnnotation> tokens = new ArrayList<StringAnnotation>();
		int length = 0;
		int docPos = 0;
		for(String string : annotationStrings){
			StringAnnotation ga = new StringAnnotation(string, length, length+string.length());
			length = string.length();
			Collection<StringAnnotation> tokAnnotations = annotate(ga);
			for(StringAnnotation ann : tokAnnotations){
				int currPos = ann.getDocumentPosition() == null ? 0 : ann.getDocumentPosition().getPosition();
				ann.setDocumentPosition(currPos + docPos);
				docPos++;
			}
			tokens.addAll(tokAnnotations);
		}
		return tokens;
	}

	public synchronized Collection<StringAnnotation> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException{
		
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		String docStr = annotation.getAnnotation();

		Span[] tokenSpans = tokeniser.tokenizePos(docStr);
		for(int i = 0; i < tokenSpans.length; i++){
			StringAnnotation token = new StringAnnotation(docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()), tokenSpans[i].getStart(), tokenSpans[i].getEnd());
			token.setDocumentPosition(i);
			annotations.add(token);
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

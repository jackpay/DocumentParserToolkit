package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class TokenAnnotator extends AbstractStringAnnotator{
	
	private TokenizerME tokeniser;

	/**
	 * Creates token annotations for a single annotation. Applying a document position annotation for each token in order. 
	 */
	public synchronized Map<IndexToken, StringAnnotation> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException{
		
		Map<IndexToken, StringAnnotation> annotations = new HashMap<IndexToken, StringAnnotation>();
		String docStr = annotation.getAnnotation();

		Span[] tokenSpans = tokeniser.tokenizePos(docStr);
		for(int i = 0; i < tokenSpans.length; i++){
			StringAnnotation token = new StringAnnotation(docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()), tokenSpans[i].getStart() + annotation.getStart(), tokenSpans[i].getEnd() + annotation.getStart());
			token.setDocumentPosition(i);
			annotations.put(token.getOffset(), token);
		}
		return annotations;
	}
	
	/**
	 * Starts the model if it has not already done so.
	 */
	public boolean modelStarted() {
		return tokeniser != null;
	}
	
	/**
	 * Checks if the model has been instantiated.
	 */
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

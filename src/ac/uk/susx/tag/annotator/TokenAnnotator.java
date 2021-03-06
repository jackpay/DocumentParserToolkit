package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class TokenAnnotator extends AbstractStringAnnotator{
	
	private TokenizerME tokeniser;
	private final boolean lowerCase;
	
	public TokenAnnotator(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	/**
	 * Creates token annotations for a single annotation. Applying a document position annotation for each token in order. 
	 */
	public synchronized List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException{
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		String docStr = annotation.getAnnotation();

		Span[] tokenSpans = tokeniser.tokenizePos(docStr);
		for(int i = 0; i < tokenSpans.length; i++){
			final String strToken = lowerCase ? docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()).toLowerCase() : docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd());
			StringAnnotation token = new StringAnnotation(strToken, tokenSpans[i].getStart() + annotation.getStart(), tokenSpans[i].getEnd() + annotation.getStart());
			token.addIndexToken(new PositionIndexToken(i));
			annotations.add(token);
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

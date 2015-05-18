package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class TokenAnnotator extends AbstractAnnotator<String,String>{
	
	private TokenizerME tokeniser;
	private boolean lower = true;
	
	/**
	 * Creates token annotations for a single annotation. Applying a document position annotation for each token in order. 
	 */
	public synchronized List<Annotation<String>> annotate(Annotation<String> annotation) throws IncompatibleAnnotationException{
		
		List<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		String docStr = annotation.getAnnotation();
		Span[] tokenSpans = tokeniser.tokenizePos(docStr);
		for(int i = 0; i < tokenSpans.length; i++){
			StringAnnotation token;
			if(lower) {
				token = new StringAnnotation(docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()).toLowerCase(), tokenSpans[i].getStart() + annotation.getStart(), tokenSpans[i].getEnd() + annotation.getStart());
			}
			else{
				token = new StringAnnotation(docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()), tokenSpans[i].getStart() + annotation.getStart(), tokenSpans[i].getEnd() + annotation.getStart());
			}
			token.addIndex(new PositionIndexToken(i));
			annotations.add(token);
		}
		return annotations;
	}
	
	public void setLower(boolean lower) {
		this.lower = lower;
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

	public List<Annotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		sentence.addAnnotations(this.getClass(), annotate(sentence.getSentence()));
		try {
			return sentence.getSentenceAnnotations(this.getClass());
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		return null;
	}
}

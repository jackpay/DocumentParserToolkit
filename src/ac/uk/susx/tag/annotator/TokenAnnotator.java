package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;
import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.SentenceAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.StringAnnotatorEnum;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class TokenAnnotator extends AbstractAnnotator<String,String,String>{
	
	private TokenizerME tokeniser;

	public IDocument<String> annotate(IDocument<String> document) throws IncompatibleAnnotationException {
		List<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		if(document.sentencesEmpty()){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(document);
		}
		else{
			Iterator<SentenceAnnotation> sentences = document.getSentenceIterator();
			while(sentences.hasNext()){
				annotations.addAll(annotate(sentences.next()));
			}
		}
		document.addAnnotations(this.getClass(), annotations);
		return document;
	}
	
	/**
	 * Creates token annotations for a single annotation. Applying a document position annotation for each token in order. 
	 */
	public synchronized List<IAnnotation<String>> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException{
		
		List<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		String docStr = annotation.getAnnotation();

		Span[] tokenSpans = tokeniser.tokenizePos(docStr);
		for(int i = 0; i < tokenSpans.length; i++){
			StringAnnotation token = new StringAnnotation(docStr.substring(tokenSpans[i].getStart(),tokenSpans[i].getEnd()), tokenSpans[i].getStart() + annotation.getStart(), tokenSpans[i].getEnd() + annotation.getStart());
			//System.err.println(token.getAnnotation() + " " + token.getStart() + " " + token.getEnd());
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

	public List<IAnnotation<String>> annotate(SentenceAnnotation sentence)
			throws IncompatibleAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}
}

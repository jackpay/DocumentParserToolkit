package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.SentenceAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class StringSentenceAnnotator extends AbstractSentenceAnnotator <String,String>{

	private SentenceDetectorME sentencetagger;
	
	public void annotate(IDocument<String> doc) throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		StringAnnotation ga = new StringAnnotation(docStr,0,docStr.length());
		ArrayList<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		annotations.addAll(annotate(ga));
		doc.addAnnotations(this.getClass(), annotations);
	}
	
	public synchronized List<SentenceAnnotation> annotate(IAnnotation<String> annotation)
			throws IncompatibleAnnotationException {
		ArrayList<SentenceAnnotation> annotations = new ArrayList<SentenceAnnotation>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		int offset = 0;
		for(int i = 0; i < sentPos.length; i++){
			StringAnnotation sentence = new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),sentPos[i].getStart() + offset,sentPos[i].getEnd() + offset);
			sentence.addIndexToken(new PositionIndexToken(i));
			annotations.add(sentence);
			offset = sentPos[i].getEnd();
		}
		annotations.
		return annotations;
	}
	
	public void startModel() {
		if(!modelStarted()){
			try {
				sentencetagger = new SentenceDetectorME(new SentenceModel(this.getClass().getClassLoader().getResourceAsStream("ensent.bin")));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean modelStarted() {
		return sentencetagger != null;
	}
}

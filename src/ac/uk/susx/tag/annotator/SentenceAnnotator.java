package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class SentenceAnnotator extends AbstractStringAnnotator {
	
	private SentenceDetectorME sentencetagger;

	public void annotate(Document<String,String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		StringAnnotation ga = new StringAnnotation(docStr,0,docStr.length());
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		annotations.addAll(annotate(ga));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public synchronized Collection<StringAnnotation> annotate(Annotation<String> annotation)
			throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		int offset = 0;
		for(int i = 0; i < sentPos.length; i++){
			StringAnnotation sentence = new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),sentPos[i].getStart() + offset,sentPos[i].getEnd() + offset);
			sentence.setDocumentPosition(i);
			System.err.println(sentence.getDocumentPosition().getPosition());
			annotations.add(sentence);
			offset = sentPos[i].getEnd();
		}
		
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

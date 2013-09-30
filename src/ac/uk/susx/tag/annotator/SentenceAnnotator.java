package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class SentenceAnnotator extends AbstractStringAnnotator {
	
	private SentenceDetectorME sentencetagger;

	public void annotate(Document<String,String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		StringAnnotation ga = new StringAnnotation(docStr,0,docStr.length());
		Map<IndexToken, Annotation<String>> annotations = new HashMap<IndexToken, Annotation<String>>();
		annotations.putAll(annotate(ga));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public synchronized Map<IndexToken, StringAnnotation> annotate(Annotation<String> annotation)
			throws IncompatibleAnnotationException {
		Map<IndexToken, StringAnnotation> annotations = new HashMap<IndexToken, StringAnnotation>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		int offset = 0;
		for(int i = 0; i < sentPos.length; i++){
			StringAnnotation sentence = new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),sentPos[i].getStart() + offset,sentPos[i].getEnd() + offset);
			sentence.setDocumentPosition(i);
			annotations.put(sentence.getOffset(), sentence);
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

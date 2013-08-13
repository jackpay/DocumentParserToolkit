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

public class SentenceAnnotator implements Annotator<Document <String,String>, StringAnnotation, String, String>{
	
	private SentenceDetectorME sentencetagger;

	public void annotate(Document<String,String> doc)
			throws IncompatibleAnnotationException {
		annotate(doc, true);
	}

	public void annotate(Document<String,String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		StringAnnotation ga = new StringAnnotation(docStr,0,docStr.length());
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		annotations.addAll(annotate(ga));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public Collection<StringAnnotation> annotate(Collection<? extends Annotation<String>> annotations)
			throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotationArr = new ArrayList<StringAnnotation>();
		int index = 0;
		for(Annotation<String> annotation : annotations){
			Collection<StringAnnotation> sentAnn = annotate(annotation);
			for(StringAnnotation ann : sentAnn){
				int currPos = ann.getDocumentPosition() == null ? 0 : ann.getDocumentPosition().getPosition();
				ann.setDocumentPosition(currPos + index);
				index++;
			}
			annotationArr.addAll(sentAnn);
		}
		return annotationArr;
	}

	public synchronized Collection<StringAnnotation> annotate(Annotation<String> annotation)
			throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		for(int i = 0; i < sentPos.length; i++){
			StringAnnotation sentence = new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),sentPos[i].getStart(),sentPos[i].getEnd());
			sentence.setDocumentPosition(i);
			annotations.add(sentence);
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

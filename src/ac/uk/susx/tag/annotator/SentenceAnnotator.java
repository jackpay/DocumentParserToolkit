package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.GrammaticalAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public final class SentenceAnnotator implements Annotator<Document <String,String>, GrammaticalAnnotation, String, String>{
	
	private SentenceDetectorME sentencetagger;

	public void annotate(Document<String,String> doc)
			throws IncompatibleAnnotationException {
		annotate(doc, true);
	}

	public void annotate(Document<String,String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		GrammaticalAnnotation ga = new GrammaticalAnnotation(docStr,0,docStr.length());
		ArrayList<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		annotations.addAll(annotate(ga));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public Collection<GrammaticalAnnotation> annotate(Collection<? extends Annotation<String>> annotations)
			throws IncompatibleAnnotationException {
		ArrayList<GrammaticalAnnotation> annotationArr = new ArrayList<GrammaticalAnnotation>();
		for(Annotation<String> annotation : annotations){
			annotationArr.addAll(annotate(annotation));
		}
		return annotationArr;
	}

	public synchronized Collection<GrammaticalAnnotation> annotate(Annotation<String> annotation)
			throws IncompatibleAnnotationException {
		ArrayList<GrammaticalAnnotation> annotations = new ArrayList<GrammaticalAnnotation>();
		String[] sentences = sentencetagger.sentDetect(annotation.getAnnotation());
		int begin = 0;
		for(int i = 0; i < sentences.length; i++){
			Pattern pattern = Pattern.compile(Pattern.quote(sentences[i]));
			Matcher matcher = pattern.matcher(annotation.getAnnotation());
			matcher.find(begin);
			GrammaticalAnnotation ga = new GrammaticalAnnotation(sentences[i], annotation.getStart() + matcher.start(), annotation.getStart() + matcher.end());
			annotations.add(ga);
			begin = matcher.end();
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

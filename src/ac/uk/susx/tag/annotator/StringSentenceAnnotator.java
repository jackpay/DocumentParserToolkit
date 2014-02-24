package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class StringSentenceAnnotator extends AbstractAnnotator <Map<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>,String,String>{

	private SentenceDetectorME sentencetagger;
	
	public IDocument<String> annotate(IDocument<String> doc) throws IncompatibleAnnotationException {
		String docStr = doc.getDocument();
		StringAnnotation ga = new StringAnnotation(docStr,0,docStr.length());
		ArrayList<Sentence<String>> annotations = new ArrayList<Sentence<String>>();
		annotations.addAll(annotate(ga));
		doc.addAllSentences(annotations);
		return doc;
	}
	
	public synchronized List<Sentence<String>> annotate(IAnnotation<String> annotation)
			throws IncompatibleAnnotationException {
		ArrayList<Sentence<String>> annotations = new ArrayList<Sentence<String>>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		int offset = 0;
		for(int i = 0; i < sentPos.length; i++){
			int startOffset = sentPos[i].getStart() + offset;
			int endOffset = sentPos[i].getEnd() + offset;
			Sentence<String> sentence = new Sentence<String>(new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),startOffset,endOffset),startOffset,endOffset);
			sentence.addIndexToken(new PositionIndexToken(i));
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

	public List<Sentence<String>> annotate(Sentence<String> sentence) throws IncompatibleAnnotationException {
		return new ArrayList<Sentence<String>>(Arrays.asList(sentence));
	}
	
}
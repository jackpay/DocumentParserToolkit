package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.SentenceAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

public class SentenceAnnotator extends AbstractAnnotator <Sentence,String>{

	private SentenceDetectorME sentencetagger;
	
	public IDocument annotate(IDocument doc) throws IncompatibleAnnotationException {
		CharSequence docStr = doc.getDocument();
		System.err.println("annotating");
		StringAnnotation ga = new StringAnnotation(docStr.toString(),0,docStr.length());
		ArrayList<IAnnotation<Sentence>> annotations = new ArrayList<IAnnotation<Sentence>>();
		annotations.addAll(annotate(ga));
		for(IAnnotation<Sentence> sentence : annotations) {
			doc.addSentence(sentence.getAnnotation());
		}
		return doc;
	}
	
	public synchronized List<? extends IAnnotation<Sentence>> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		List<IAnnotation<Sentence>> annotations = new ArrayList<IAnnotation<Sentence>>();
		Span[] sentPos = sentencetagger.sentPosDetect(annotation.getAnnotation());
		
		int offset = 0;
		for(int i = 0; i < sentPos.length; i++){
			int startOffset = sentPos[i].getStart() + offset;
			int endOffset = sentPos[i].getEnd() + offset;
			Sentence sentence = new Sentence(new StringAnnotation(annotation.getAnnotation().substring(sentPos[i].getStart(),sentPos[i].getEnd()),startOffset,endOffset),startOffset,endOffset);
			sentence.getSentence().addIndexToken(new PositionIndexToken(i));
			annotations.add(new SentenceAnnotation(sentence,sentence.getSentence().getStart(),sentence.getSentence().getEnd()));
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

	public List<? extends IAnnotation<Sentence>> annotate(Sentence sentence)
			throws IncompatibleAnnotationException {
		return new ArrayList<IAnnotation<Sentence>>(Arrays.asList(new SentenceAnnotation(sentence,sentence.getSentence().getStart(),sentence.getSentence().getEnd())));
	}
	
}

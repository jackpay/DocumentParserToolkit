package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractAnnotator <AT,ACT> implements IAnnotator<AT,ACT> {
	
	private Class<? extends IAnnotatorFactory<Sentence,String>> sentence = SentenceAnnotatorFactory.class;

	public IDocument annotate(IDocument document) throws IncompatibleAnnotationException {
		if(document.sentencesEmpty()){
			try {
				AnnotatorRegistry.getAnnotator(sentence).annotate(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("HERe");
		Iterator<Sentence> sentences = document.getSentenceIterator();
		while(sentences.hasNext()){
			annotate(sentences.next());
		}
		return document;
	}

	public List<IAnnotation<AT>> annotate(List<? extends IAnnotation<ACT>> annotations) throws IncompatibleAnnotationException {
		ArrayList<IAnnotation<AT>> annotationArr = new ArrayList<IAnnotation<AT>>();
		int index = 0;
		for(IAnnotation<ACT> annotation : annotations){
			List<? extends IAnnotation<AT>> sentAnn = annotate(annotation);
			for(IAnnotation<AT> ann : sentAnn){
				int currPos = 0;
				try {
					currPos = ann.getIndexToken(PositionIndexToken.class) == null ? 0 : ann.getIndexToken(PositionIndexToken.class).getPosition();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ann.addIndexToken(new PositionIndexToken(currPos + index));
				index++;
			}
			annotationArr.addAll(sentAnn);
		}
		return annotationArr;
	}
	
	public void setSentenceAnnotator(Class<? extends IAnnotatorFactory<Sentence,String>> annotator) {
		this.sentence = annotator;
	}

}

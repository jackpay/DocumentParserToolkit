package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractAnnotator <AT,ACT> implements IAnnotator<AT,ACT> {
	
	private Class<? extends IAnnotatorFactory<Sentence,String>> sentence = SentenceAnnotatorFactory.class;

	public Document annotate(Document document) throws IncompatibleAnnotationException {
		if(document.isEmpty()){
			try {
				AnnotatorRegistry.getAnnotator(sentence).annotate(document);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Iterator<Sentence> sentences = document.iterator();
		while(sentences.hasNext()){
			annotate(sentences.next());
		}
		return document;
	}

	public List<Annotation<AT>> annotate(List<? extends Annotation<ACT>> annotations) throws IncompatibleAnnotationException {
		ArrayList<Annotation<AT>> annotationArr = new ArrayList<Annotation<AT>>();
		int index = 0;
		for(Annotation<ACT> annotation : annotations){
			List<? extends Annotation<AT>> sentAnn = annotate(annotation);
			for(Annotation<AT> ann : sentAnn){
				int currPos = 0;
				try {
					currPos = ann.getIndex(PositionIndexToken.class) == null ? 0 : ann.getIndex(PositionIndexToken.class).getPosition();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ann.addIndex(new PositionIndexToken(currPos + index));
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

package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.SentenceAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.StringAnnotatorEnum;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractAnnotator <AT,DT,ACT> implements IAnnotator<AT,DT,ACT> {
	
	public AbstractAnnotator() {}

	public IDocument<DT> annotate(IDocument<DT> document) throws IncompatibleAnnotationException {
		List<IAnnotation<AT>> annotations = new ArrayList<IAnnotation<AT>>();
		if(document.sentencesEmpty()){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(document);
		}
		else{
			Iterator<SentenceAnnotation<ACT>> sentences = document.getSentenceIterator();
			while(sentences.hasNext()){
				annotations.addAll(annotate(sentences.next()));
			}
		}
		document.addAnnotations((Class<? extends IAnnotator<AT,?,?>>) this.getClass(), annotations);
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

}

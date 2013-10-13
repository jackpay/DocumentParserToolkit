package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.StringAnnotatorEnum;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.indexing.PositionIndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractStringAnnotator implements IAnnotator<IDocument<String>, StringAnnotation, String, String> {

	public void annotate(IDocument<String> document)
			throws IncompatibleAnnotationException {
		annotate(document,true);
	}

	public void annotate(IDocument<String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		List<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		List<? extends IAnnotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
		if(sentences == null){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(doc);
		}
		sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass()); 
		annotations.addAll(annotate(sentences));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public List<StringAnnotation> annotate(List<? extends IAnnotation<String>> annotations)
			throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotationArr = new ArrayList<StringAnnotation>();
		int index = 0;
		for(IAnnotation<String> annotation : annotations){
			Collection<StringAnnotation> sentAnn = annotate(annotation);
			for(StringAnnotation ann : sentAnn){
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

package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractStringAnnotator implements IAnnotator<IDocument<String,String>, StringAnnotation, String, String> {

	public void annotate(IDocument<String, String> document)
			throws IncompatibleAnnotationException {
		annotate(document,true);
	}

	public void annotate(IDocument<String, String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		Collection<IAnnotation<String>> annotations = new ArrayList<IAnnotation<String>>();
		Collection<? extends IAnnotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
		if(sentences == null){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(doc);
		}
		sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass()); 
		annotations.addAll(annotate(sentences));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public Collection<StringAnnotation> annotate(Collection<? extends IAnnotation<String>> annotations)
			throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotationArr = new ArrayList<StringAnnotation>();
		int index = 0;
		for(IAnnotation<String> annotation : annotations){
			Collection<StringAnnotation> sentAnn = annotate(annotation);
			for(StringAnnotation ann : sentAnn){
				int currPos = ann.getPosition() == null ? 0 : ann.getPosition().getPosition();
				ann.setDocumentPosition(currPos + index);
				index++;
			}
			annotationArr.addAll(sentAnn);
		}
		return annotationArr;
	}

}

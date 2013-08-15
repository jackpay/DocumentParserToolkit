package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractStringAnnotator implements Annotator<Document<String,String>, StringAnnotation, String, String> {

	public void annotate(Document<String, String> document)
			throws IncompatibleAnnotationException {
		annotate(document,true);
	}

	public void annotate(Document<String, String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		Collection<Annotation<String>> annotations = new ArrayList<Annotation<String>>();
		Collection<? extends Annotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
		if(sentences == null){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(doc);
		}
		sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass()); 
		annotations.addAll(annotate(sentences));
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

}

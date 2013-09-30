package ac.uk.susx.tag.annotator;

import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public abstract class AbstractStringAnnotator implements Annotator<Document<String,String>, StringAnnotation, String, String> {

	public void annotate(Document<String, String> document)
			throws IncompatibleAnnotationException {
		annotate(document,true);
	}

	public void annotate(Document<String, String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		Map<IndexToken, Annotation<String>> annotations = new HashMap<IndexToken, Annotation<String>>();
		Map<IndexToken, ? extends Annotation<String>> sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass());
		if(sentences == null){
			StringAnnotatorEnum.SENTENCE.getAnnotator().annotate(doc);
		}
		sentences = doc.getAnnotations(StringAnnotatorEnum.SENTENCE.getAnnotator().getClass()); 
		annotations.putAll(annotate(sentences));
		doc.addAnnotations(this.getClass(), annotations);
	}

	public Map<IndexToken, StringAnnotation> annotate(Map<IndexToken, ? extends Annotation<String>> annotations)
			throws IncompatibleAnnotationException {
		HashMap<IndexToken, StringAnnotation> annotationMap = new HashMap<IndexToken, StringAnnotation>();
		int index = 0;
		for(IndexToken annotation : annotations.keySet()){
			Map<IndexToken, StringAnnotation> sentAnn = annotate(annotations.get(annotation));
			for(IndexToken ann : sentAnn.keySet()){
				int currPos = sentAnn.get(ann).getPosition() == null ? 0 : sentAnn.get(ann).getPosition().getPosition();
				sentAnn.get(ann).setDocumentPosition(currPos + index);
				index++;
			}
			annotationMap.putAll(sentAnn);
		}
		return annotationMap;
	}

}

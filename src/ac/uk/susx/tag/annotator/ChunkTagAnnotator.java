package ac.uk.susx.tag.annotator;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.GrammaticalAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class ChunkTagAnnotator implements Annotator<Document<String,String>, GrammaticalAnnotation, String>{

	public void startModel() {
		// TODO Auto-generated method stub
		
	}

	public boolean modelStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void annotate(Document<String, String> doc)
			throws IncompatibleAnnotationException {
		// TODO Auto-generated method stub
		
	}

	public void annotate(Document<String, String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		// TODO Auto-generated method stub
		
	}

	public Collection<GrammaticalAnnotation> annotate(
			Collection<? extends Annotation<String>> annotations)
			throws IncompatibleAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<GrammaticalAnnotation> annotate(
			Annotation<String> annotation)
			throws IncompatibleAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

}

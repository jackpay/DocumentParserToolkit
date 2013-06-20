package ac.uk.susx.tag.annotation.annotators;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotations.GrammaticalAnnotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;

/**
 * An annotator which can annotate a document using its raw text from the Document object
 * or annotate a pre-tokenised collection.
 * @author jackpay
 *
 */
public class PoSTagAnnotator implements Annotator<StringDocument, GrammaticalAnnotation>{

	public void annotate(StringDocument doc) {
		annotate(doc, true);
	}

	public void annotate(Collection<? extends Annotation> annotations) {
		// TODO Auto-generated method stub
		
	}

	public void annotate(StringDocument doc, boolean parseRawText) {
		// TODO Auto-generated method stub
		
	}

	public void startModel() {
		// TODO Auto-generated method stub
		
	}

	public boolean modelStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void annotate(Collection<GrammaticalAnnotation> annotations) {
		// TODO Auto-generated method stub
		
	}

}

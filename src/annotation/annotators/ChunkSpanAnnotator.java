package annotation.annotators;

import java.util.Collection;

import javax.swing.text.Document;

import annotation.Annotation;
import annotation.Annotator;

public class ChunkSpanAnnotator implements Annotator{

	public void annotate(Document doc) {
		annotate(doc, true);
	}

	public void annotate(Collection<Annotation> annotations) {
	}

	public void annotate(Document doc, boolean parseRawText) {
		// TODO Auto-generated method stub
		
	}

	public void startModel() {
		// TODO Auto-generated method stub
		
	}

	public boolean modelStarted() {
		// TODO Auto-generated method stub
		return false;
	}

}

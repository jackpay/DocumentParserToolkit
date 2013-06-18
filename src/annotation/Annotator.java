package annotation;

import java.util.Collection;

import javax.swing.text.Document;

public interface Annotator {
	
	public void annotate(Document doc);
	
	public void annotate(Document doc, boolean parseRawText);
	
	public void annotate(Collection<Annotation> annotations);
	
	public void startModel();
	
	public boolean modelStarted();
}

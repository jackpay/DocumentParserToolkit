package ac.uk.susx.tag.annotation.annotator;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.document.Document;

public interface Annotator <D extends Document, A extends Annotation>{
	
	public void annotate(D doc);
	
	public void annotate(D doc, boolean parseRawText);
	
	public Collection<? extends Annotation> annotate(Collection<A> annotations);
	
	public void startModel();
	
	public boolean modelStarted();
	
}

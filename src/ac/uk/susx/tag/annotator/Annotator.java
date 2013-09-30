package ac.uk.susx.tag.annotator;

import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface Annotator <D extends Document<DT,AT>, A extends Annotation<AT>,AT,DT> {
	
	public void annotate(D document) throws IncompatibleAnnotationException;
	
	public void annotate(D doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public Map<IndexToken,A> annotate(Map<IndexToken, ? extends Annotation<AT>> annotations) throws IncompatibleAnnotationException;
	
	public Map<IndexToken,A> annotate(Annotation<AT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

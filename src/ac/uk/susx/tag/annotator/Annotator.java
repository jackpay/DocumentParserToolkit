package ac.uk.susx.tag.annotator;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface Annotator <D extends Document<DT,AT>, A extends Annotation<AT>,AT,DT>{
	
	public void annotate(D document) throws IncompatibleAnnotationException;
	
	public void annotate(D doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public Collection<A> annotate(Collection<? extends Annotation<AT>> annotations) throws IncompatibleAnnotationException;
	
	public Collection<A> annotate(Annotation<AT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

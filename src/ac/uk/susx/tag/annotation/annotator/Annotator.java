package ac.uk.susx.tag.annotation.annotator;

import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public interface Annotator <D extends Document<?,?>, A extends Annotation<AT>, AT>{
	
	public void annotate(D doc) throws IncompatibleAnnotationException;
	
	public void annotate(D doc, boolean parseRawText) throws IncompatibleAnnotationException;
	
	public Collection<A> annotate(Collection<? extends Annotation<AT>> annotations) throws IncompatibleAnnotationException;
	
	public Collection<A> annotate(Annotation<AT> annotation) throws IncompatibleAnnotationException;
	
	public void startModel();
	
	public boolean modelStarted();
	
}

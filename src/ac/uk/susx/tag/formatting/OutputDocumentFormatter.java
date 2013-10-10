package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.writer.IOutputWriter;

public interface OutputDocumentFormatter <W,AT>{
	
	public void processDocument(String outputFileName, Map<IIndexToken, Collection<IAnnotation<AT>>> sortedCollection);
	
	public void processSubDocument(IOutputWriter<W> writer, Map<IIndexToken, Collection<IAnnotation<AT>>> sortedCollection);
	
}

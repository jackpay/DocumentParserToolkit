package ac.uk.susx.tag.formatting;

import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.writer.OutputWriter;

public interface OutputDocumentFormatter <W,AT>{
	
	public void processDocument(String outputFileName, Map<IndexToken, Collection<Annotation<AT>>> sortedCollection);
	
	public void processSubDocument(OutputWriter<W> writer, Map<IndexToken, Collection<Annotation<AT>>> sortedCollection);
	
}

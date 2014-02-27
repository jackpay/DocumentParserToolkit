package ac.uk.susx.tag.formatting;

import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.writer.IOutputWriter;

public interface IOutputDocumentFormatter <W>{
	
	public void processDocument(String outputFileName, Map<IIndexToken,List<IAnnotation<?>>> sortedCollection);
	
	public void processSubDocument(IOutputWriter<W> writer, Map<IIndexToken,List<IAnnotation<?>>> sortedCollection);
	
}

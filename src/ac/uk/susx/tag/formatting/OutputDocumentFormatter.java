package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.writer.OutputWriter;

public interface OutputDocumentFormatter <D,AT>{
	
	public void processDocument(Document<D,AT> outputDocument, String outputFileName, Class<? extends Annotator> head);
	
	public void processDocument(Document<D,AT> outputDocument, String outputFileName);
	
	public void processSubDocument(Document<D,AT> outputDocument,OutputWriter<String> writer);
	
	public void processSubDocument(Document<D, AT> outputDocument,OutputWriter<String> writer, Class<? extends Annotator> head);
	
}

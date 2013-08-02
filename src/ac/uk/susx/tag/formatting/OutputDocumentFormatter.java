package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

public interface OutputDocumentFormatter <D,AT>{
	
	public void processOutput(Document<D,AT> outputDocument, String outputFileName, Class<? extends Annotator> head);
	
	public void processOutput(Document<D,AT> outputDocument, String outputFileName);
	
}

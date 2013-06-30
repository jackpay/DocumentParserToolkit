package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

public interface OutputDocumentFormatter <AT>{
	
	public void processOutput(Document<?,AT> outputDocument, String outputFileName, Class<? extends Annotator> head);
	
	public void processOutput(Document<?,AT> outputDocument, String outputFileName);
	
}

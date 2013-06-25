package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

public interface OutputDocumentFormatter <A,AT>{
	
	public void processOutput(Document<A,AT> outputDocument, String outputFileName, Class<? extends Annotator> head);
	
	public void processOutput(Document<A,AT> outputDocument, String outputFileName);
	
}

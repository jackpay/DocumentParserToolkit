package ac.uk.susx.tag.formatting.document.output;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.writer.IOutputWriter;

public interface IOutputDocumentFormatter {
	
	public void processDocument(String outputFileName, Document document);
	
	public void processSubDocument(IOutputWriter writer, Document document);
	
}

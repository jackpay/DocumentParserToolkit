package ac.uk.susx.tag.formatting.document.output;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.writer.OutputWriter;

public interface IOutputDocumentFormatter {
	
	public void processDocument(OutputWriter writer, Document document);
	
}

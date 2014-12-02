package ac.uk.susx.tag.formatting.document.output;

import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.writer.IOutputWriter;

public interface IOutputDocumentFormatter {
	
	public void processDocument(String outputFileName, IDocument document);
	
	public void processSubDocument(IOutputWriter writer, IDocument document);
	
}

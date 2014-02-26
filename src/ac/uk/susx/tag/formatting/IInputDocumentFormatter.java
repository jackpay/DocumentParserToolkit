package ac.uk.susx.tag.formatting;

import java.io.File;

import ac.uk.susx.tag.document.IDocument;

public interface IInputDocumentFormatter { 
	
	public IDocument createDocument(String fileLocation);
	
	public IDocument createDocument(File file);
	
}

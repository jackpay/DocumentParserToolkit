package ac.uk.susx.tag.formatting;

import java.io.File;

import ac.uk.susx.tag.document.IDocument;

public interface IInputDocumentFormatter <DT> { 
	
	public IDocument<DT> createDocument(String fileLocation);
	
	public IDocument<DT> createDocument(File file);
	
}

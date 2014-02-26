package ac.uk.susx.tag.formatting;

import java.io.File;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.IDocument;

public class BasicInputDocumentFormatter implements IInputDocumentFormatter{

	public IDocument createDocument(String fileLocation) {
		return createDocument(new File(fileLocation));
	}

	public IDocument createDocument(File file) {
		return new Document(file);
	}
}

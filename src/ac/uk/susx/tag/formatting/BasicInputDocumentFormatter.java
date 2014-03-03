package ac.uk.susx.tag.formatting;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.IDocument;

public class BasicInputDocumentFormatter implements IInputDocumentFormatter{

	public IDocument createDocument(String file) {
		return createDocument(file);
	}

	public IDocument createDocument(File file) {
		String f = null;
		try {
			f = FileUtils.fileRead(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Document(f);
	}
	
}

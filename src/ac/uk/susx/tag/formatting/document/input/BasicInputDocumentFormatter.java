package ac.uk.susx.tag.formatting.document.input;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import ac.uk.susx.tag.document.Document;

public class BasicInputDocumentFormatter implements IInputDocumentFormatter{

	public Document createDocument(CharSequence file) {
		return new Document(file);
	}

	public Document createDocument(File file) {
		String f = null;
		try {
			f = FileUtils.readFileToString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Document(f);
	}
	
}

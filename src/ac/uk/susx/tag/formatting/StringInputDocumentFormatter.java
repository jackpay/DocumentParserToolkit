package ac.uk.susx.tag.formatting;

import java.io.File;
import java.io.IOException;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.FileUtils;

public class StringInputDocumentFormatter implements InputDocumentFormatter<String,String>{

	public Document<String, String> createDocument(String fileLocation) {
		return createDocument(new File(fileLocation));
	}

	public Document<String, String> createDocument(File file) {
		String rawDoc = null;
		try{
			rawDoc = FileUtils.readFileAsString(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringDocument sd = new StringDocument(rawDoc);
		return sd;
	}
}

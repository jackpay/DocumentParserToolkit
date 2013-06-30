package ac.uk.susx.tag.formatting;

import java.io.IOException;

import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.StringDocument;
import ac.uk.susx.tag.utils.ParserUtils;

public class StringInputDocumentFormatter implements InputDocumentFormatter<String,String>{

	public Document<String, String> createDocument(String fileLocation) {
		String rawDoc = null;
		try {
			rawDoc = ParserUtils.readFileAsString(fileLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringDocument sd = new StringDocument(rawDoc);
		return sd;
	}
}

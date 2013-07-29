package ac.uk.susx.tag.document;

public class StringDocument extends AbstractDocument <String,String> {

	public StringDocument(String rawDoc) {
		super(rawDoc);
	}

	public String getDocumentSubSection(int start) {
		return getDocument().substring(start);
	}

	public String getDocumentSubSection(int start, int end) {
		return getDocument().substring(start, end);
	}
	
}

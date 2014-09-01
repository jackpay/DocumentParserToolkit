package ac.uk.susx.tag.formatting.document.input;

import java.io.File;
import java.io.IOException;

import ac.uk.susx.tag.document.Document;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import ac.uk.susx.tag.document.IDocument;

import org.apache.commons.io.FileUtils;

public class HTMLStripperDocumentFormatter implements IInputDocumentFormatter {
	

	@Override
	public IDocument createDocument(CharSequence doc) {
		return new Document(parseHTML(doc.toString()));
	}

	@Override
	public IDocument createDocument(File file) {
        try {
            return new Document(parseHTML(FileUtils.readFileToString(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	
	private String parseHTML(String html) {
		String doc = null;
		try {
			doc = ArticleExtractor.INSTANCE.getText(html);
		} catch (BoilerpipeProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.replace("\\n", " ").replace("\\t", " ");
	}
}

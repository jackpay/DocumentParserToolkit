package ac.uk.susx.tag.formatting.document.input;

import java.io.File;
import java.io.IOException;

import ac.uk.susx.tag.document.Document;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

import org.apache.commons.io.FileUtils;

public class HTMLStripperDocumentFormatter implements IInputDocumentFormatter {
	

	@Override
	public Document createDocument(CharSequence doc, CharSequence name) {
		return new Document(parseHTML(doc.toString()), name);
	}

	@Override
	public Document createDocument(File file) {
        try {
            return new Document(parseHTML(FileUtils.readFileToString(file)), file.getName());
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
			e.printStackTrace();
		}
		return doc.replace("\\n", " ").replace("\\t", " ");
	}
}

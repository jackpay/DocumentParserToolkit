package ac.uk.susx.tag.formatting;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;

/**
 * Used to parse and html document.
 * @author jp242
 *
 */
public class HTMLStripperDocumentFormatter implements InputDocumentFormatter<String,String> {

	/**
	 * Creates strips off the html markup for the html document at a given location.
	 */
	public IDocument<String, String> createDocument(String fileLocation) {
		return createDocument(new File(fileLocation));
	}

	public IDocument<String, String> createDocument(File file) {
		try {
			return new StringDocument(parseHTML(FileUtils.readFileToString(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Takes the html as a string and strips all markup
	 * @param html The html document as a String
	 * @return the string with all markup removed
	 */
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

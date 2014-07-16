package ac.uk.susx.tag.formatting;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;

public class MedlineDocumentFormatter implements InputDocumentFormatter<String,String> {
	
	public IDocument<String, String> createDocument(String fileLocation) {
		return createDocument(new File(fileLocation));
	}

	public IDocument<String, String> createDocument(File file) {
		
		String rawDoc = "";
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList list = doc.getElementsByTagName("AbstractText");
			for(int i = 0; i < list.getLength(); i++){
				rawDoc = list.item(i).getTextContent();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringDocument sd = new StringDocument(rawDoc);
		return sd;
	}

}

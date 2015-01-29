package ac.uk.susx.tag.formatting.document.input;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ac.uk.susx.tag.document.Document;

public class MedlineAbstractDocumentFormatter implements IInputDocumentFormatter {
	
	private static final String TEXT_NODE = "AbstractText";

	@Override
	public Document createDocument(CharSequence rawDoc, CharSequence docName) {
		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(rawDoc.toString()));
		    org.w3c.dom.Document doc = db.parse(is);
		    NodeList nList = doc.getElementsByTagName(TEXT_NODE);
			String text = nList.item(0).getTextContent();
			return new Document(text, docName);
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
		return null;
	}

	@Override
	public Document createDocument(File file) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(file);
			NodeList nList = doc.getElementsByTagName(TEXT_NODE);
			String text = nList.item(0).getTextContent();
			return new Document(text, file.getName());
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
		return null;
	}

}

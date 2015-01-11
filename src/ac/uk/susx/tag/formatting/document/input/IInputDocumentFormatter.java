package ac.uk.susx.tag.formatting.document.input;

import java.io.File;

import ac.uk.susx.tag.document.Document;

public interface IInputDocumentFormatter { 
	
	/**
	 * A method to take a raw document as a CharSequence and output as a correctly formatted Document object
	 * @param rawDoc The raw document represented as a CharSequence
	 * @return The document formatted and output as a Document.
	 */
	public Document createDocument(CharSequence rawDoc, CharSequence docName);
	
	/**
	 * Takes a File object and outputs the file at that location as a Document object.
	 * @param file The File representing the input document.
	 * @return The file formatted as a Document object.
	 */
	public Document createDocument(File file);
	
}

package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.document.Document;

public interface InputDocumentFormatter <AT>{ 
	
	public Document<?,AT> createDocument(String fileLocation);
	
}

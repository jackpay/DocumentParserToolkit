package ac.uk.susx.tag.formatting;

import ac.uk.susx.tag.document.Document;

public interface InputDocumentFormatter <DT,AT>{ 
	
	public Document<DT,AT> createDocument(String fileLocation);
	
}

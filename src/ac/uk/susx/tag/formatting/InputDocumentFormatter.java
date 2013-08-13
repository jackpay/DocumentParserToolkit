package ac.uk.susx.tag.formatting;

import java.io.File;

import ac.uk.susx.tag.document.Document;

public interface InputDocumentFormatter <DT,AT>{ 
	
	public Document<DT,AT> createDocument(String fileLocation);
	
	public Document<DT,AT> createDocument(File file);
	
}

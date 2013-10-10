package ac.uk.susx.tag.formatting;

import java.io.File;

import ac.uk.susx.tag.document.IDocument;

public interface InputDocumentFormatter <DT,AT>{ 
	
	public IDocument<DT,AT> createDocument(String fileLocation);
	
	public IDocument<DT,AT> createDocument(File file);
	
}

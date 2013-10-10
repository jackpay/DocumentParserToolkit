package ac.uk.susx.tag.configuration;

import ac.uk.susx.tag.document.IDocument;

public class StringConfiguration extends AbstractConfiguration <IDocument<String,String>,String,String>{

	public StringConfiguration(String inputLoc, String outputLoc) {
		super(inputLoc, outputLoc);
	}
	
}

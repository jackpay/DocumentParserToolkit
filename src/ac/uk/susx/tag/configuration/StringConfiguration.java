package ac.uk.susx.tag.configuration;

import ac.uk.susx.tag.document.Document;

public class StringConfiguration extends AbstractConfiguration <Document<String,String>,String,String>{

	public StringConfiguration(String inputLoc, String outputLoc) {
		super(inputLoc, outputLoc);
	}
	
}

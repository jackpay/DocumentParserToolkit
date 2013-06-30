package ac.uk.susx.tag.input;

import ac.uk.susx.tag.configuration.Configuration;

public interface InputParameterParser {
	
	public Configuration<?,?> parseInputParameters(String[] args);

}

package ac.uk.susx.tag.input;

import ac.uk.susx.tag.configuration.IConfiguration;

public interface IInputParameterParser {
	
	public IConfiguration<?,?,?> parseInputParameters(String[] args);

}

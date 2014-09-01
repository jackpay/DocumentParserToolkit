package ac.uk.susx.tag.utils;

public class IllegalInputParamsException extends Exception {
	
	private static final String MESSAGE = "The format of the additional additional input parameters is wrong. Annotator command line options must begin with a '-', and their additional parameters (if any) should follow, and must not start '-'";
	
	public IllegalInputParamsException(){
		super(MESSAGE);
	}

}
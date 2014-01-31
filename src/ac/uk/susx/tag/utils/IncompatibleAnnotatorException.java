package ac.uk.susx.tag.utils;

public class IncompatibleAnnotatorException extends Exception{
	
	private static final long serialVersionUID = 3123665721239938321L;
	
	private static final String MESSAGE = "This class of IAnnotator is not compatible with the calling IAnnotator.";
	
	public IncompatibleAnnotatorException(){
		super(MESSAGE);
	}

}

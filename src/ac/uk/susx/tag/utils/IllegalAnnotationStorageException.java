package ac.uk.susx.tag.utils;

public class IllegalAnnotationStorageException extends Exception{
	
	private static final String MESSAGE = "The Annotation(s) stored at this location were not created by an Annotator of this class.";
	
	private static final long serialVersionUID = 5183425872608872157L;
	
	public IllegalAnnotationStorageException(Class<?> cl) {
		super(MESSAGE + cl.getName());
	}

}

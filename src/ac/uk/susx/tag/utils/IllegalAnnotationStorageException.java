package ac.uk.susx.tag.utils;

public class IllegalAnnotationStorageException extends Exception{
	
	private static final String MESSAGE = "The Annotation(s) stored at this location is were not created by " +
			"the passing class:";
	
	private static final long serialVersionUID = 5183425872608872157L;
	
	public IllegalAnnotationStorageException(Class<?> cl) {
		super(MESSAGE + cl.getName());
	}

}

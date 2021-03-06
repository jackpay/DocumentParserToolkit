package ac.uk.susx.tag.utils;

public class IncompatibleAnnotationException extends Exception{
	
	private static final String MESSAGE = "This class of annotation is incompatible with this annotator. Was " +
			"expecting class: ";

	private static final long serialVersionUID = -1748730026260397510L;
	
	public IncompatibleAnnotationException(Class<?> expectedClass){
		super(MESSAGE + expectedClass.getName());
	}

}

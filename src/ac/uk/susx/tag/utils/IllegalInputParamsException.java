package ac.uk.susx.tag.utils;

import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;

public class IllegalInputParamsException extends Exception {
	
	private static final long serialVersionUID = 6489111828869839521L;
	private static final String MESSAGE_P1 = "The input parameters given to instantiate the class ";
	private static final String MESSAGE_P2 = " were not correct.";
	
	public IllegalInputParamsException(Class<? extends IAnnotatorFactory<?,?>> failedClass){
		super(MESSAGE_P1 + failedClass + MESSAGE_P2);
	}

}
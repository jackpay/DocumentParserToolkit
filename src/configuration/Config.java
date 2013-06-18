package configuration;

import java.util.ArrayList;

import annotation.AnnotationTypes;
import annotation.Annotator;

/**
 * The abstract class for a global config file.
 * @author jackpay
 *
 */
public abstract class Config {
	
	private ArrayList<Annotator> annotators; // Specify the annotators to use when parsing.
	private ArrayList<Annotator> includeAnnotators; // Specify which annotators should be included in the output.
	
	public Config(){
		addAnnotator(AnnotationTypes.SENTENCE.getAnnotator());
		addAnnotator(AnnotationTypes.TOKEN.getAnnotator());
	}
	
	public void addAnnotator(Annotator annotator){
		addAnnotator(annotator,true);
	}
	
	/**
	 * Adds the annotator to the list of annotators which will be applied to the text.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(Annotator annotator, boolean include){
		if(!annotator.modelStarted()){		// Starts the annotators model if necessary but not already done.
			annotator.startModel();
		}
		annotators.add(annotator);
		if(include){
			includeAnnotators.add(annotator);	// Adds to collection of annotators required in the output.
		}
	}
	
}

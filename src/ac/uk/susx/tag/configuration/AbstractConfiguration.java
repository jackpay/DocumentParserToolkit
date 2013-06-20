package ac.uk.susx.tag.configuration;

import java.util.ArrayList;

import ac.uk.susx.tag.annotation.annotator.Annotator;
import ac.uk.susx.tag.annotation.annotator.AnnotatorTypes;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<A,T> implements Configuration<A,T>{
	
	private ArrayList<Annotator> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Annotator> includeAnnotators; // Specify which annotator's should be included in the output.
	
	public AbstractConfiguration(){
		addAnnotator(AnnotatorTypes.TOKEN.getAnnotator(), true); // Ensure that tokens are created.
	}
	
	public void addAnnotator(Annotator annotator){
		addAnnotator(annotator, false);
	}
	
	public void addAnnotator(Annotator annotator, boolean include){
		if(!annotator.modelStarted()){		// Starts the annotator's model if necessary.
			annotator.startModel();
		}
		annotators.add(annotator);
		if(include){
			includeAnnotators.add(annotator);	// Adds to collection of annotator's required in the output.
		}
	}
	
}

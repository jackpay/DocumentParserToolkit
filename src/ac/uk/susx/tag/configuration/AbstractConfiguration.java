package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<D extends Document<?,AT>, A extends Annotation<AT>, AT > implements Configuration<D, A, AT> {
	
	private ArrayList<Annotator<D,A,AT>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Annotator<D,A,AT>> includeAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	
	public AbstractConfiguration(String inputLoc, String outputLoc){
		this.inputLoc = inputLoc;
		this.outputLoc = outputLoc;
	}
	
	public String getInputLocation() {
		return inputLoc;
	}

	public String getOutputLocation() {
		return outputLoc;
	}
	
	public void setInputSuff(String inputSuff){
		this.inputSuff = inputSuff;
	}
	
	public void setOutSuff(String outputSuff){
		this.outputSuff = outputSuff;
	}
	
	public String getInputSuff(){
		return inputSuff;
	}
	
	public String getOutputSuff(){
		return outputSuff;
	}
	
	public Collection<Annotator<D,A,AT>> getAnnotators(){
		return annotators;
	}
	
	public Collection<Annotator<D,A,AT>> getOutputIncludedAnnotators(){
		return includeAnnotators;
	}
	
	public void addAnnotator(Annotator<D,A,AT> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(Annotator<D,A,AT> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includeAnnotators.add(annotator);
		}
	}
}

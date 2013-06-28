package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<D extends Document<?,AT>, AT> implements Configuration<D,AT> {
	
	private ArrayList<Annotator<D,? extends Annotation<AT>,AT>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Annotator<D,? extends Annotation<AT>,AT>> includeAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	private boolean singleFile;
	
	public AbstractConfiguration(String inputLoc, String outputLoc){
		this.inputLoc = inputLoc;
		this.outputLoc = outputLoc;
		annotators = new ArrayList<Annotator<D,? extends Annotation<AT>,AT>>();
		includeAnnotators = new ArrayList<Annotator<D,? extends Annotation<AT>,AT>>();
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
	
	public void setSingleFileOutput(boolean single){
		this.singleFile = single;
	}
	
	public boolean singleFileOutput(){
		return singleFile;
	}
	
	public String getInputSuff(){
		return inputSuff;
	}
	
	public String getOutputSuff(){
		return outputSuff;
	}
	
	public Collection<Annotator<D,? extends Annotation<AT>,AT>> getAnnotators(){
		return annotators;
	}
	
	public Collection<Annotator<D,? extends Annotation<AT>,AT>> getOutputIncludedAnnotators(){
		return includeAnnotators;
	}
	
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includeAnnotators.add(annotator);
		}
	}
}

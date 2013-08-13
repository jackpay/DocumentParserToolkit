package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<D extends Document<DT,AT>, AT,DT> implements Configuration<D,AT,DT> {
	
	private ArrayList<Annotator<D,? extends Annotation<AT>,AT,DT>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Class<? extends Annotator>> includedAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	private boolean singleFile;
	private OutputDocumentFormatter<DT,AT> outputWriter;
	private InputDocumentFormatter<DT,AT> docBuilder;
	private Class<? extends Annotator> headAnnotator;
	
	public AbstractConfiguration(String inputLoc, String outputLoc){
		this.inputLoc = inputLoc;
		this.outputLoc = outputLoc;
		annotators = new ArrayList<Annotator<D,? extends Annotation<AT>,AT,DT>>();
		includedAnnotators = new ArrayList<Class<? extends Annotator>>();
		headAnnotator = StringAnnotatorEnum.TOKEN.getAnnotator().getClass(); // Defaults the head annotator to Token but can be changed.
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
	
	public Collection<Annotator<D,? extends Annotation<AT>,AT,DT>> getAnnotators(){
		return annotators;
	}
	
	public Collection<Class<? extends Annotator>> getOutputIncludedAnnotators(){
		return includedAnnotators;
	}
	
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT,DT> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT,DT> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includedAnnotators.add((Class<Annotator<D, ? extends Annotation<AT>, AT,DT>>) annotator.getClass());
		}
	}
	
	/**
	 * Set the document output writer.
	 */
	public void setOutputWriter(OutputDocumentFormatter<DT,AT> outputWriter){
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Get the document output writer.
	 */
	public OutputDocumentFormatter<DT,AT> getOutputWriter(){
		return outputWriter;
	}
	
	/**
	 * Set the Document object builder.
	 */
	public void setDocumentBuilder(InputDocumentFormatter<DT,AT> documentBuilder){
		docBuilder = documentBuilder;
	}
	
	/**
	 * Get the Document object builder
	 * @return
	 */
	public InputDocumentFormatter<DT,AT> getDocumentBuilder(){
		return docBuilder;
	}
	
	/**
	 * Used to specify what the head annotation for the output should be.
	 * @return
	 */
	public Class<? extends Annotator> getHeadAnnotator(){
		return headAnnotator;
	}
	
	/**
	 * Used to set the annotator, which should have its output at the head of each output token.
	 */
	public void setHeadAnnotator(Class<? extends Annotator> head){
		headAnnotator = head;
	}
}

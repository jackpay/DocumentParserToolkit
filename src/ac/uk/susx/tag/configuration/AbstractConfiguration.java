package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;
import ac.uk.susx.tag.utils.FileUtils;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<D extends IDocument<DT,AT>, AT,DT> implements IConfiguration<D,AT,DT> {
	
	private ArrayList<IAnnotator<D,? extends IAnnotation<AT>,AT,DT>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Class<? extends IAnnotator>> includedAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	private boolean singleFile;
	private OutputDocumentFormatter<DT,AT> outputWriter;
	private InputDocumentFormatter<DT,AT> docBuilder;
	private ArrayList<IFilter<AT>> filters;
	
	public AbstractConfiguration(String inputLoc, String outputLoc) {
		this.inputLoc = inputLoc;
		this.outputLoc = FileUtils.createOutputDirectory(outputLoc);
		annotators = new ArrayList<IAnnotator<D,? extends IAnnotation<AT>,AT,DT>>();
		includedAnnotators = new ArrayList<Class<? extends IAnnotator>>();
		filters = new ArrayList<IFilter<AT>>();
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
	
	public Collection<IAnnotator<D,? extends IAnnotation<AT>,AT,DT>> getAnnotators(){
		return annotators;
	}
	
	public ArrayList<Class<? extends IAnnotator>> getOutputIncludedAnnotators(){
		return includedAnnotators;
	}
	
	public void addAnnotator(IAnnotator<D,? extends IAnnotation<AT>,AT,DT> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(IAnnotator<D,? extends IAnnotation<AT>,AT,DT> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includedAnnotators.add((Class<IAnnotator<D, ? extends IAnnotation<AT>, AT,DT>>) annotator.getClass());
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
	 * Return the annotation filters.
	 */
	public Collection<IFilter<AT>> getFilters(){
		return filters;
	}
	
	public void addFilter(IFilter<AT> filter){
		filters.add(filter);
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
	
}

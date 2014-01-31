package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.formatting.IInputDocumentFormatter;
import ac.uk.susx.tag.formatting.IOutputDocumentFormatter;
import ac.uk.susx.tag.utils.FileUtils;

/**
 * An abstract class for a global config file.
 * @author jackpay
 */
public abstract class AbstractConfiguration<AT,DT> implements IConfiguration<AT,DT> {
	
	private ArrayList<IAnnotator<?,?,?>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Class<? extends IAnnotator<?,?,?>>> includedAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	private boolean singleFile;
	private IOutputDocumentFormatter<DT,AT> outputWriter;
	private IInputDocumentFormatter<DT> docBuilder;
	private ArrayList<IFilter<?>> filters;
	
	public AbstractConfiguration(String inputLoc, String outputLoc) {
		this.inputLoc = inputLoc;
		this.outputLoc = FileUtils.createOutputDirectory(outputLoc);
		annotators = new ArrayList<IAnnotator<?,?,?>>();
		includedAnnotators = new ArrayList<Class<? extends IAnnotator<?,?,?>>>();
		filters = new ArrayList<IFilter<?>>();
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
	
	public Collection<IAnnotator<?,?,?>> getAnnotators(){
		return annotators;
	}
	
	public ArrayList<Class<? extends IAnnotator<?,?,?>>> getOutputIncludedAnnotators(){
		return includedAnnotators;
	}
	
	public void addAnnotator(IAnnotator<?,?,?> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(IAnnotator<?,?,?> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includedAnnotators.add((Class<? extends IAnnotator<?,?,?>>) annotator.getClass());
		}
	}
	
	/**
	 * Set the document output writer.
	 */
	public void setOutputWriter(IOutputDocumentFormatter<DT,AT> outputWriter){
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Get the document output writer.
	 */
	public IOutputDocumentFormatter<DT,AT> getOutputWriter(){
		return outputWriter;
	}
	
	/**
	 * Return the annotation filters.
	 */
	public Collection<IFilter<?>> getFilters(){
		return filters;
	}
	
	public void addFilter(IFilter<?> filter){
		filters.add(filter);
	}
	
	/**
	 * Set the Document object builder.
	 */
	public void setDocumentBuilder(IInputDocumentFormatter<DT> documentBuilder){
		docBuilder = documentBuilder;
	}
	
	/**
	 * Get the Document object builder
	 * @return
	 */
	public IInputDocumentFormatter<DT> getDocumentBuilder(){
		return docBuilder;
	}
	
	
	
}

package ac.uk.susx.tag.configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.formatting.document.input.IInputDocumentFormatter;
import ac.uk.susx.tag.formatting.document.output.IOutputDocumentFormatter;
import ac.uk.susx.tag.utils.FileUtils;
import ac.uk.susx.tag.writer.OutputWriter;

/**
 * An default class for a global config file.
 * @author jackpay
 */
public class StandardConfiguration implements IConfiguration {
	
	private ArrayList<IAnnotator<?,?>> annotators; // Specify the annotator's to use when parsing.
	private ArrayList<Class<? extends IAnnotator<?,?>>> includedAnnotators; // Specify which annotator's should be included in the output.
	private final String inputLoc;
	private final String outputLoc;
	private String inputSuff;
	private String outputSuff;
	private boolean singleFile;
	private IOutputDocumentFormatter outputFormatter;
	private IInputDocumentFormatter docBuilder;
	private ArrayList<IFilter<?>> filters;
	private final DocumentWriterFactory writerFactory;
	
	public StandardConfiguration(String inputLoc, String outputLoc) {
		this.inputLoc = inputLoc;
		this.outputLoc = FileUtils.createOutputDirectory(outputLoc);
		annotators = new ArrayList<IAnnotator<?,?>>();
		includedAnnotators = new ArrayList<Class<? extends IAnnotator<?,?>>>();
		filters = new ArrayList<IFilter<?>>();
		writerFactory = new DocumentWriterFactory();
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
	
	public Collection<IAnnotator<?,?>> getAnnotators(){
		return annotators;
	}
	
	public ArrayList<Class<? extends IAnnotator<?,?>>> getOutputIncludedAnnotators(){
		return includedAnnotators;
	}
	
	public void addAnnotator(IAnnotator<?,?> annotator){
		annotators.add(annotator);
	}
	
	public void addAnnotator(IAnnotator<?,?> annotator, boolean include){
		annotators.add(annotator);
		if(include){
			includedAnnotators.add((Class<? extends IAnnotator<?,?>>) annotator.getClass());
		}
	}
	
	/**
	 * Set the document output writer.
	 */
	public void setOutputWriter(IOutputDocumentFormatter outputWriter){
		this.outputFormatter = outputWriter;
	}
	
	/**
	 * Get the document output writer.
	 */
	public IOutputDocumentFormatter getOutputWriter(){
		return outputFormatter;
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
	public void setDocumentBuilder(IInputDocumentFormatter documentBuilder){
		docBuilder = documentBuilder;
	}
	
	/**
	 * Get the Document object builder
	 * @return
	 */
	public IInputDocumentFormatter getDocumentBuilder(){
		return docBuilder;
	}
	
	private class DocumentWriterFactory {
		
		private OutputWriter getWriter(String outputLoc) throws IOException {
			return new OutputWriter(outputLoc);
		}
		
		private OutputWriter getWriter(File outFile) throws IOException {
			return new OutputWriter(outputLoc);
		}
	}


	@Override
	public OutputWriter getWriter(String outputLoc) {
		try {
			return writerFactory.getWriter(outputLoc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OutputWriter getWriter(File outFile) {
		try {
			return writerFactory.getWriter(outFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setOutputFormatter(IOutputDocumentFormatter outputFormatter) {
		this.outputFormatter = outputFormatter;
	}

	@Override
	public IOutputDocumentFormatter getOutputFormatter() {
		return outputFormatter;
	}
	
}

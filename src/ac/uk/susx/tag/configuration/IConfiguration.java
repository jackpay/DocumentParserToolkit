package ac.uk.susx.tag.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.formatting.document.input.IInputDocumentFormatter;
import ac.uk.susx.tag.formatting.document.output.IOutputDocumentFormatter;
import ac.uk.susx.tag.writer.OutputWriter;

/**
 * The interace class for a global config file.
 * @author jackpay
 *
 */
public interface IConfiguration {	
	
	/**
	 * Add an annotator to the collection of annotators and specify if its annotations will be included in the output.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(IAnnotator<?,?> annotator, boolean include);
	
	/**
	 * @return Return all stored annotators.
	 */
	public Collection<IAnnotator<?,?>> getAnnotators();
	
	/**
	 * Return all stored filters;
	 * @return
	 */
	public Collection<IFilter<?>> getFilters();
	
	/**
	 * Add a filter.
	 * @param filter
	 */
	public void addFilter(IFilter<?> filter);
	
	/**
	 * @return Return all annotators which will have all their annotations included in the output. Uses ArrayList to guarantee order.
	 */
	public ArrayList<Class<? extends IAnnotator<?,?>>> getOutputIncludedAnnotators();
	
	/**
	 * @return Return the output location.Class<? extends Annotator>
	 */
	public String getInputLocation();
	
	/**
	 * @return Return the input location.
	 */
	public String getOutputLocation();
	
	/**
	 * Set the document output writer.
	 */
	public void setOutputFormatter(IOutputDocumentFormatter outputFormatter);
	
	/**
	 * Get the document output writer.
	 */
	public IOutputDocumentFormatter getOutputFormatter();
	
	/**
	 * Set the Document object builder.
	 */
	public void setDocumentBuilder(IInputDocumentFormatter documentBuilder);
	
	/**
	 * Get the Document object builder
	 * @return
	 */
	public IInputDocumentFormatter getDocumentBuilder();
	
	/**
	 * Set the suffix for input documents.
	 * @return
	 */
	public String getInputSuff();
	
	/**
	 * Set the suffix to be applied to output documents
	 * @return
	 */
	public String getOutputSuff();
	
	/**
	 * Get an OutputWriter for file location.
	 */
	public OutputWriter getWriter(String outputLoc);
	
	/**
	 * Get an OutputWriter for file.
	 */
	public OutputWriter getWriter(File outFile);

}
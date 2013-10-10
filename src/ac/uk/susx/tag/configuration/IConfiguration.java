package ac.uk.susx.tag.configuration;

import java.util.ArrayList;
import java.util.Collection;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;

/**
 * The interace class for a global config file.
 * @author jackpay
 *
 */
public interface IConfiguration <D extends IDocument<DT,AT>, AT,DT >{	
	
	/**
	 * Add an annotator to the collection of annotators to use.
	 * @param annotator
	 */
	public void addAnnotator(IAnnotator<D,? extends IAnnotation<AT>,AT,DT> annotator);
	
	/**
	 * Add an annotator to the collection of annotators and specify if its annotations will be included in the output.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(IAnnotator<D,? extends IAnnotation<AT>,AT,DT> annotator, boolean include);
	
	/**
	 * @return Return all stored annotators.
	 */
	public Collection<IAnnotator<D,? extends IAnnotation<AT>,AT,DT>> getAnnotators();
	
	/**
	 * Return all stored filters;
	 * @return
	 */
	public Collection<IFilter<AT>> getFilters();
	
	/**
	 * Add a filter.
	 * @param filter
	 */
	public void addFilter(IFilter<AT> filter);
	
	/**
	 * @return Return all annotators which will have all their annotations included in the output. Uses ArrayList to guarantee order.
	 */
	public ArrayList<Class<? extends IAnnotator>> getOutputIncludedAnnotators();
	
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
	public void setOutputWriter(OutputDocumentFormatter<DT,AT> outputWriter);
	
	/**
	 * Get the document output writer.
	 */
	public OutputDocumentFormatter<DT,AT> getOutputWriter();
	
	/**
	 * Set the Document object builder.
	 */
	public void setDocumentBuilder(InputDocumentFormatter<DT,AT> documentBuilder);
	
	/**
	 * Get the Document object builder
	 * @return
	 */
	public InputDocumentFormatter<DT,AT> getDocumentBuilder();
	
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

}
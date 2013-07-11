package ac.uk.susx.tag.configuration;

import java.util.Collection;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.formatting.InputDocumentFormatter;
import ac.uk.susx.tag.formatting.OutputDocumentFormatter;

/**
 * The interace class for a global config file.
 * @author jackpay
 *
 */
public interface Configuration <D extends Document<DT,AT>, AT,DT >{	
	
	/**
	 * Add an annotator to the collection of annotators to use.
	 * @param annotator
	 */
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT,DT> annotator);
	
	/**
	 * Add an annotator to the collection of annotators and specify if its annotations will be included in the output.
	 * @param annotator
	 * @param include
	 */
	public void addAnnotator(Annotator<D,? extends Annotation<AT>,AT,DT> annotator, boolean include);
	
	/**
	 * @return Return all stored annotators.
	 */
	public Collection<Annotator<D,? extends Annotation<AT>,AT,DT>> getAnnotators();
	
	/**
	 * @return Return all annotators which will have all their annotations included in the output.
	 */
	public Collection<Class<? extends Annotator>> getOutputIncludedAnnotators();
	
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
	public void setOutputWriter(OutputDocumentFormatter<AT> outputWriter);
	
	/**
	 * Get the document output writer.
	 */
	public OutputDocumentFormatter<AT> getOutputWriter();
	
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
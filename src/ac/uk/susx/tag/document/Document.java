package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public class Document implements IDocument {
	
	private final List<Sentence> sentences; // The document represented as Sentences.
	private CharSequence document; // The raw text of the document.
	private CharSequence id; // Allows a document to have an identifier. Different Document objects are able to have the same id, for purposes of document consolidation.
	
	public Document(CharSequence document){
		sentences = new ArrayList<Sentence>();
		this.document = document;
	}
	
	public Document(CharSequence document, CharSequence id){
		this(document);
		this.id = id;
	}
	
	/**
	 * Returns a CharSequence representation of the raw document.
	 */
	public CharSequence getDocument(){
		return document;
	}

	/**
	 * Retrieve a SentenceAnnotation from a specified position.
	 */
	public Sentence getSentence(int pos) {
		return sentences.get(pos);
	}

	/**
	 * Retrieve the iterator object for list of SentenceAnnotations.
	 */
	public Iterator<Sentence> getSentenceIterator() {
		Iterator<Sentence> iter = sentences.iterator();
		return iter;
	}

	/**
	 * Add a SentenceAnnotation to a specified position in the list.
	 */
	public void addSentence(Sentence sent, int pos) {
		sentences.add(pos, sent);
	}

	/**
	 * Add a SentenceAnnotation to the end of list.
	 */
	public void addSentence(Sentence sentence) {
		sentences.add(sentence);
	}

	/**
	 * Returns true if there are no sentences contained in the Document object.
	 */
	public boolean sentencesEmpty() {
		return sentences.isEmpty();
	}

	/**
	 * Adds a list of Sentence objects to the Documents Sentence list.
	 */
	public void addAllSentences(List<Sentence> sentences) {
		sentences.addAll(sentences);
	}

	/**
	 * Returns all Annotations of a given class contained the Document objects Sentence list.
	 */
	public <AT> List<IAnnotation<AT>> getDocumentAnnotations(Class<? extends IAnnotator<AT, ?>> cl) throws IllegalAnnotationStorageException {
		List<IAnnotation<AT>> anns = new ArrayList<IAnnotation<AT>>();
		for(Sentence sentence : sentences){
			anns.addAll(sentence.getSentenceAnnotations(cl));
		}
		return anns;
	}

	/**
	 * Removes all IAnnotation objects created by a given class list of IAnnotator from all the Documents Sentences. 
	 */
	public void removeDocumentAnnotations(Collection<Class<? extends IAnnotator<?, ?>>> excludedAnnotators) {
		for(Sentence sentence : sentences) {
			sentence.removeAnnotations(excludedAnnotators);
		}
	}
	
	/**
	 * Retains all IAnnotation objects created by the given IAnnotator class list, from the Document's Sentences.
	 */
	public void retainDocumentAnnotations(Collection<Class<? extends IAnnotator<?, ?>>> includedAnnotators) {
		for(Sentence sentence : sentences) {
			sentence.retainAnnotations(includedAnnotators);
		}
	}
	/**
	 * Applies a collection of IFilter to all IAnnotations in the Document objects Sentence list.
	 */
	public void filterDocumentAnnotations(Collection<IFilter<?>> filters) {
		for(Sentence sentence : sentences) {
			sentence.filterAnnotations(filters);
		}
	}

	/**
	 * Sets the id for the document.
	 */
	public void setDocumentId(CharSequence id) {
		this.id = id;
	}

	/**
	 * Returns the id for the document.
	 */
	public CharSequence getDocumentId() {
		return id;
	}
	
	public String toString() {
		return document.toString();
	}

}

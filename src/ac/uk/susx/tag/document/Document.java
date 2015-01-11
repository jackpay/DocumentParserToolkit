package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public final class Document extends ArrayList<Sentence> {
	
	private static final long serialVersionUID = -8795126134577485984L;
	private CharSequence document; // The raw text of the document.
	private CharSequence name; // The name of the document.
	private int id; // Allows a document to have an identifier. Different Document objects are able to have the same id, for purposes of document consolidation
	
	public Document(CharSequence document, CharSequence name){
		this.document = document;
		this.name = name;
	}
	
	/**
	 * Returns a CharSequence representation of the raw document.
	 */
	public CharSequence getDocument(){
		return document;
	}
	
	/**
	 * Sets the id for the document.
	 */
	public void setDocumentId(int id) {
		this.id = id;
	}

	/**
	 * Returns the id for the document.
	 */
	public int getDocumentId() {
		return id;
	}
	
	/**
	 * Returns the name of the document.
	 */
	public CharSequence getName() {
		return name;
	}
	
	public String toString() {
		return document.toString();
	}

	/**
	 * Returns all Annotations of a given class contained the Document objects Sentence list.
	 */
	public <AT> List<Annotation<AT>> getDocumentAnnotations(Class<? extends IAnnotator<AT, ?>> cl) throws IllegalAnnotationStorageException {
		List<Annotation<AT>> anns = new ArrayList<Annotation<AT>>();
		for(Sentence sentence : this){
			anns.addAll(sentence.getSentenceAnnotations(cl));
		}
		return anns;
	}

	/**
	 * Removes all IAnnotation objects created by a given class list of IAnnotator from all the Documents Sentences. 
	 */
	public void removeDocumentAnnotations(Collection<Class<? extends IAnnotator<?, ?>>> excludedAnnotators) {
		for(Sentence sentence : this) {
			sentence.removeAnnotations(excludedAnnotators);
		}
	}
	
	/**
	 * Retains all IAnnotation objects created by the given IAnnotator class list, from the Document's Sentences.
	 */
	public void retainDocumentAnnotations(Collection<Class<? extends IAnnotator<?, ?>>> includedAnnotators) {
		for(Sentence sentence : this) {
			sentence.retainAnnotations(includedAnnotators);
		}
	}
	/**
	 * Applies a collection of IFilter to all IAnnotations in the Document objects Sentence list.
	 */
	public void filterDocumentAnnotations(Collection<IFilter<?>> filters) {
		for(Sentence sentence : this) {
			sentence.filterAnnotations(filters);
		}
	}

}
package ac.uk.susx.tag.document;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.filter.IFilter;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public interface IDocument {
	
	public CharSequence getDocument();
	
	public Sentence getSentence(int pos);
	
	public boolean sentencesEmpty();
	
	public Iterator<Sentence> getSentenceIterator();
	
	public void addSentence(Sentence sent, int pos);
	
	public void addSentence(Sentence sentence);
	
	public void addAllSentences(List<Sentence> sentences);

	public <AT> List<IAnnotation<AT>> getDocumentAnnotations(Class<? extends IAnnotator<AT,?>> cl) throws IllegalAnnotationStorageException;
	
	//public Collection<List<? extends IAnnotation<?>>> getAllDocumentAnnotations();
	
	public void removeDocumentAnnotations(Collection<Class<? extends IAnnotator<?,?>>> excludedAnnotators);
	
	public void retainDocumentAnnotations(Collection<Class<? extends IAnnotator<?,?>>> includedAnnotators);
	
	public void filterDocumentAnnotations(Collection<IFilter<?>> filters);
	
	public void setDocumentId(CharSequence id);
	
	public CharSequence getDocumentId();
	
}

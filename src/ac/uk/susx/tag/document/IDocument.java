package ac.uk.susx.tag.document;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public interface IDocument{
	
	public File getDocument();
	
	public Sentence getSentence(int pos);
	
	public boolean sentencesEmpty();
	
	public Iterator<Sentence> getSentenceIterator();
	
	public void addSentence(Sentence sent, int pos);
	
	public void addSentence(Sentence sentence);
	
	public void addAllSentences(List<Sentence> sentences);
//
//	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?,?>> cl, List<? extends IAnnotation<AT>> annotations);
//	
//	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator<AT,?,?>> cl);
//	
//	public Collection<List<? extends IAnnotation<?>>> getDocumentAnnotations();
//	
//	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> excludedAnnotators);
//	
//	public void removeAnnotation(Class<? extends IAnnotator<?,?,?>> cl);
//	
//	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> includedAnnotators);
//	
//	public void filterAnnotations(Collection<IFilter<?>> filters);
//	
//	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?,?>> annotator);
	
}

package ac.uk.susx.tag.document;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Document implements IDocument {
	
	//private final D document;
	//private final Map<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>> annotations;
	private final List<Sentence> sentences;
	private File document;
	
	public Document(File document){
		//this.document = rawDoc;
		//annotations = new HashMap<Class<? extends IAnnotator<?,?,?>>, List<? extends IAnnotation<?>>>(10);
		sentences = new ArrayList<Sentence>();
		this.document = document;
	}
	
	public File getDocument(){
		return document;
	}

	/**
	 * Retrieve a SentenceAnnotation from a specified position
	 */
	public Sentence getSentence(int pos) {
		return sentences.get(pos);
	}

	/**
	 * Retrieve the iterator object for list of SentenceAnnotations
	 */
	public Iterator<Sentence> getSentenceIterator() {
		Iterator<Sentence> iter = sentences.iterator();
		return iter;
	}

	/**
	 * Add a SentenceAnnotation to a specified position in the list
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

	public boolean sentencesEmpty() {
		return sentences.isEmpty();
	}

	public void addAllSentences(List<Sentence> sentences) {
		sentences.addAll(sentences);
	}

//	public <AT> List<IAnnotation<AT>> getAnnotations(Class<? extends IAnnotator<AT,?,?>> cl) {
//		return (List<IAnnotation<AT>>) annotations.get(cl);
//	}

//	public Collection<List<? extends IAnnotation<?>>> getDocumentAnnotations() {
//		return annotations.values();
//	}

//	public <AT> void addAnnotations(Class<? extends IAnnotator<AT,?,?>> cl, List<? extends IAnnotation<AT>> annotations) {
//		List<IAnnotation<AT>> anns = (List<IAnnotation<AT>>) this.annotations.get(cl);
//		if(anns == null){
//			this.annotations.put(cl, new ArrayList<IAnnotation<AT>>());
//			anns = (List<IAnnotation<AT>>) this.annotations.get(cl);
//			anns.addAll(annotations);
//		}
//		else{
//			anns.addAll(annotations);
//		}
//	}

//	public void removeAnnotation(Class<? extends IAnnotator<?,?,?>> cl) {
//		if(annotations.containsKey(cl)){
//			annotations.remove(cl);
//		}
//	}
//
//	public void removeAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> annotators) {
//		for(Class<? extends IAnnotator<?,?,?>> annotator : annotators){
//			removeAnnotation(annotator);
//		}
//	}

//	public void retainAnnotations(Collection<Class<? extends IAnnotator<?,?,?>>> includedAnnotators) {
//		annotations.keySet().retainAll(includedAnnotators);
//	}
//	
//	public void filterAnnotations(Collection<IFilter<?>> filters){
//		if(filters != null && !filters.isEmpty()){
//			for(IFilter<?> filter : filters){
//				filter.filterCollection(annotations);
//			}
//		}
//	}
//	
//	//TODO: type checking - although is enforced elsewhere 
//	public <AT> void filterAnnotation(Collection<IFilter<AT>> filters, Class<? extends IAnnotator<AT,?,?>> annotator) {
//		if(filters != null && !filters.isEmpty()){
//			for(IFilter<AT> filter : filters){
//				filter.filter((List<? extends IAnnotation<AT>>) annotations.get(annotator));
//			}
//		}
//	}

}

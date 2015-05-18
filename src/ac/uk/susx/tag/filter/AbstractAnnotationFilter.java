package ac.uk.susx.tag.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public abstract class AbstractAnnotationFilter<AT>  implements IFilter<AT>{
	
	private final ArrayList<AT> filterAnnotations;
	private final Class<? extends IAnnotator<AT,?>> annotator;
	private boolean remAllTok; // Remove all tokens and annotations associated with matching annotation.
	private boolean remove; // Use to specify whether this filter removes matches or retains matches.
	
	public AbstractAnnotationFilter(List<AT> filterAnnotations, Class<? extends IAnnotator<AT,?>> annotator, boolean remAllTok, boolean remove) {
		this.filterAnnotations = new ArrayList<AT>();
		this.filterAnnotations.addAll(filterAnnotations);
		this.annotator = annotator;
		this.remAllTok = remAllTok;
		this.remove = remove;
	}
	
	public AbstractAnnotationFilter(Class<? extends IAnnotator<AT,?>> annotator, boolean remAllTok, boolean remove) {
		filterAnnotations = null;
		this.annotator = annotator;
		this.remAllTok = remAllTok;
		this.remove = remove;
	}
	
	public AbstractAnnotationFilter(AT annotation, Class<? extends IAnnotator<AT,?>> annotator, boolean remAllTok, boolean remove) {
		this(new ArrayList<AT>(), annotator, remAllTok, remove);
		filterAnnotations.add(annotation);
	}
	
	public List<Annotation<AT>> filterList(List<Annotation<AT>> annotations) {
		Iterator<? extends Annotation<AT>> iter = annotations.iterator();
		while(iter.hasNext()){
			Annotation<AT> anno = iter.next();
			if((matchAnnotation(anno) && remove) || (!matchAnnotation(anno) && !remove)) {
				iter.remove();
			}
		}
		return annotations;
	}
	
	public Sentence filterSentence(Sentence sentence) {
		
		try {
			if(sentence.getSentenceAnnotations(annotator) == null) {
				return sentence;
			}
		} catch (IllegalAnnotationStorageException e1) {
			e1.printStackTrace();
		}
		if(!remAllTok){
			try {
				filterList((List<Annotation<AT>>) sentence.getSentenceAnnotations(annotator));
			} catch (IllegalAnnotationStorageException e) {
				e.printStackTrace();
			}
		}
		else{
			Collection<Annotation<AT>> filtAnno = null;
			try {
				filtAnno = (List<Annotation<AT>>) sentence.getSentenceAnnotations(annotator);
			} catch (IllegalAnnotationStorageException e) {
				e.printStackTrace();
			}
			Iterator<Annotation<AT>> iter = filtAnno.iterator();
			while(iter.hasNext()){
				Annotation<AT> next = iter.next();
				if((matchAnnotation(next) && remove) || (!matchAnnotation(next) && !remove)){
					sentence.removeAnnotation(next.getOffset());
				}
			}
		}
		return sentence;
	}
	
	public List<AT> getFilterAnnotations() {
		return filterAnnotations;
	}
	
	public abstract boolean matchAnnotation(Annotation<AT> annotation);

}

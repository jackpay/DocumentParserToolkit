package ac.uk.susx.tag.annotator;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.DatabaseException;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.database.UnigramIndexer;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class TermFrequencyAnnotator extends AbstractStringAnnotator{
	
	private final UnigramIndexer index;
	
	public TermFrequencyAnnotator(UnigramIndexer index) {
		this.index = index;
	}

	public List<StringAnnotation> annotate(IAnnotation<String> annotation)
			throws IncompatibleAnnotationException {
		List<? extends IAnnotation<String>> tokens = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(annotation);
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		for(IAnnotation<String> token : tokens) {
			try {
				StringAnnotation sa = new StringAnnotation(String.valueOf(index.getPrimaryIndex().get(token.getAnnotation()).getFrequency()),token.getStart(),token.getEnd());
				annotations.add(sa);
			} catch (DatabaseException e) {
				e.printStackTrace();
			}
		}
		return annotations;
	}

	public void startModel() {}

	public boolean modelStarted() {
		return true;
	}
}

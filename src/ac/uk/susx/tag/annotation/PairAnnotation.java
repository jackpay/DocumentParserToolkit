package ac.uk.susx.tag.annotation;

import org.apache.commons.lang3.tuple.Pair;

public abstract class PairAnnotation<A,B> extends Annotation<Pair<A,B>>{

	public PairAnnotation(Pair<A, B> annotation, int start, int end) {
		super(annotation, start, end);
	}

}

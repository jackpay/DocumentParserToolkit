package ac.uk.susx.tag.annotation;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.token.StandardTokenFormatter;
import ac.uk.susx.tag.indexing.OffsetIndexToken;

public class SentenceOffsetPairAnnotation extends PairAnnotation<Sentence,OffsetIndexToken> {
	
	private final CharSequence DELIM;
	private final StandardTokenFormatter tokenMaker;

	public SentenceOffsetPairAnnotation(Pair<Sentence, OffsetIndexToken> annotation, int start, int end) {
		super(annotation, start, end);
		this.DELIM = "/";
		tokenMaker = new StandardTokenFormatter("/");
	}
	
	public SentenceOffsetPairAnnotation(Pair<Sentence, OffsetIndexToken> annotation, CharSequence delim,int start, int end) {
		super(annotation, start, end);
		this.DELIM = delim;
		tokenMaker = new StandardTokenFormatter(delim);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		List<Annotation<?>> anns = this.getAnnotation().getLeft().getIndexedAnnotations(this.getAnnotation().getRight());
		if(anns != null) {
			for(Annotation<?> an : anns) {
				if(!(an instanceof SentenceOffsetPairAnnotation)) {
					sb.append(an.toString() + DELIM);
				}
			}
		}
		if(sb.length() > 1) {
			return sb.substring(0, sb.length()-1);
		}
		return sb.toString();
	}

}

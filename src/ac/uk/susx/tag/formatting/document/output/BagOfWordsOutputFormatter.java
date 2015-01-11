package ac.uk.susx.tag.formatting.document.output;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.token.StandardTokenFormatter;
import ac.uk.susx.tag.writer.OutputWriter;

public class BagOfWordsOutputFormatter implements IOutputDocumentFormatter {
	
	private final CharSequence TOKEN_DELIM;

	public BagOfWordsOutputFormatter(){
		this("\t");
	}
	
	public BagOfWordsOutputFormatter(CharSequence delimiter){
		TOKEN_DELIM = delimiter;
	}

	public void processDocument(OutputWriter writer, Document document) {
		StandardTokenFormatter tokenMaker = new StandardTokenFormatter();
		for(Sentence sent : document) {
			Collection<List<? extends Annotation<?>>> groupedAnnotations = sent.getAllIndexedAnnotations();
			for(List<? extends Annotation<?>> annotations : groupedAnnotations){
				CharSequence token = tokenMaker.createToken(annotations);
				try {
					writer.write(new StringBuilder().append(token).append(TOKEN_DELIM).toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

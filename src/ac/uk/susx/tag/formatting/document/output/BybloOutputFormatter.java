package ac.uk.susx.tag.formatting.document.output;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.TokenAnnotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.token.StandardTokenFormatter;
import ac.uk.susx.tag.writer.OutputWriter;

public class BybloOutputFormatter implements IOutputDocumentFormatter {
	
	private static final Class<TokenAnnotator> TOKENISER = TokenAnnotator.class;
	private final CharSequence TOKEN_DELIM;
	
	public BybloOutputFormatter() {
		this.TOKEN_DELIM = "\t";
	}
	
	public BybloOutputFormatter(CharSequence delim) {
		this.TOKEN_DELIM = delim;
	}

	@Override
	public void processDocument(OutputWriter writer, Document document) {
		StandardTokenFormatter tokenMaker = new StandardTokenFormatter();
		for(Sentence sent : document) {
			Collection<List<Annotation<?>>> groupedAnnotations = sent.getAllIndexedAnnotations();
			for(List<? extends Annotation<?>> annotations : groupedAnnotations){
				CharSequence token = tokenMaker.createToken(annotations);
				try {
					writer.write(new StringBuilder().append(token).append(TOKEN_DELIM).toString() + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

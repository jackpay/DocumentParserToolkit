package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.writer.OutputWriter;
import ac.uk.susx.tag.writer.StringWriter;

public class StringBagOfWordsOutputDocumentFormatter implements OutputDocumentFormatter<String,String>{
	
	private final char TOKEN_DELIM;

	public StringBagOfWordsOutputDocumentFormatter(){
		this('\t');
	}
	
	public StringBagOfWordsOutputDocumentFormatter(char delimiter){
		TOKEN_DELIM = delimiter;
	}

	public void processDocument(String outputFileName, Map<IndexToken, Collection<Annotation<String>>> sortedCollection) {
		StringTokenAnnotatorFormatter tokenMaker = new StringTokenAnnotatorFormatter();
		StringWriter docWriter = null;
		try {
			docWriter = new StringWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Collection<Annotation<String>> tokenColl : sortedCollection.values()){
			String token = tokenMaker.createToken(tokenColl);
			docWriter.writeToken(token + TOKEN_DELIM);
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSubDocument(OutputWriter<String> writer, Map<IndexToken, Collection<Annotation<String>>> sortedCollection) {
		StringTokenAnnotatorFormatter tokenMaker = new StringTokenAnnotatorFormatter();
		for(Collection<Annotation<String>> tokenColl : sortedCollection.values()){
			String token = tokenMaker.createToken(tokenColl);
			try {
				writer.writeToken(token + TOKEN_DELIM);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

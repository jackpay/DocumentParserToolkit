package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
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

	public void processDocument(String outputFileName, Map<Class<? extends Annotator>, Map<IndexToken, Annotation<String>>> collection) {
		StringTokenAnnotatorFormatter tokenMaker = new StringTokenAnnotatorFormatter();
		StringWriter docWriter = null;
		try {
			docWriter = new StringWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		for(Collection<Annotation<String>> tokenColl : collection.values()){
//			String token = tokenMaker.addToken(tokenColl);
//			docWriter.writeToken(tokenMaker.createToken() + TOKEN_DELIM);
//		}
		for(Map<IndexToken, Annotation<String>> annotations : collection.values()){
			for(IndexToken key : annotations.keySet()){
				for(Map<IndexToken, Annotation<String>> subAnnotations : collection.values()){
					Annotation<String> ann = subAnnotations.remove(key);
					if(ann != null){
						tokenMaker.addToken(ann);
					}
				}
				docWriter.writeToken(tokenMaker.createToken() + TOKEN_DELIM);
			}
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSubDocument(OutputWriter<String> writer, Map<Class<? extends Annotator>, Map<IndexToken, Annotation<String>>> collection) {
		StringTokenAnnotatorFormatter tokenMaker = new StringTokenAnnotatorFormatter();
		
		for(Map<IndexToken, Annotation<String>> annotations : collection.values()){
			for(IndexToken key : annotations.keySet()){
				for(Map<IndexToken, Annotation<String>> subAnnotations : collection.values()){
					Annotation<String> ann = subAnnotations.remove(key);
					if(ann != null){
						tokenMaker.addToken(ann);
					}
				}
				try {
					writer.writeToken(tokenMaker.createToken() + TOKEN_DELIM);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		for(Collection<Annotation<String>> tokenColl : collection.values()){
//			String token = tokenMaker.createToken(tokenColl);
//			try {
//				writer.writeToken(token + TOKEN_DELIM);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

}

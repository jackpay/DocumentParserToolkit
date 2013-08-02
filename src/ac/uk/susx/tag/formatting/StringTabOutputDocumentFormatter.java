package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.ParserUtils;
import ac.uk.susx.tag.writer.StringWriter;

public class StringTabOutputDocumentFormatter implements OutputDocumentFormatter<String,String>{
	
	private static final char TOKEN_DELIM = '\t';

	public void processOutput(Document<String, String> outputDocument,
			String outputFileName) {
	}

	public void processOutput(Document<String, String> outputDocument,
			String outputFileName,
			Class<? extends Annotator> head) {
		Map<Class<? extends Annotator>, Collection<Annotation<String>>> annotations = outputDocument.getDocumentAnnotations();
		HashMap<IndexToken, ArrayList<Annotation<String>>> sortedCollection = ParserUtils.collectAnnotations(annotations, head);
		
		StringTokenHyphenFormatter tokenMaker = new StringTokenHyphenFormatter();
		StringWriter docWriter = null;
		try {
			docWriter = new StringWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(ArrayList<Annotation<String>> tokenColl : sortedCollection.values()){
			String token = tokenMaker.createToken(outputDocument.getDocument(), tokenColl);
			try {
				docWriter.writeToken(token + TOKEN_DELIM);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.ParserUtils;
import ac.uk.susx.tag.writer.StringWriter;

public class StringOutputDocumentFormatter implements OutputDocumentFormatter<String,String>{
	
	private static final String TOKEN_DELIM = "\\t";

	public void processOutput(Document<String, String> outputDocument,
			String outputFileName) {
	}

	public void processOutput(Document<String, String> outputDocument,
			String outputFileName,
			Class<? extends Annotator> head) {
		Map<Class<? extends Annotator>, Collection<Annotation<String>>> annotations = outputDocument.getDocumentAnnotations();
		HashMap<Integer, ArrayList<Annotation<String>>> sortedCollection = ParserUtils.collectAnnotations(annotations);
		
		StringTokenFormatter tokenMaker = new StringTokenFormatter();
		StringWriter docWriter = null;
		try {
			docWriter = new StringWriter(outputFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(ArrayList<Annotation<String>> tokenColl : sortedCollection.values()){
			String token = tokenMaker.createToken(tokenColl);
			System.err.println(token);
			try {
				docWriter.writeToken(token + TOKEN_DELIM);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

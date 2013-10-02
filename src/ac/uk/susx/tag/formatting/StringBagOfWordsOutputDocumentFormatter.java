package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;
import ac.uk.susx.tag.utils.AnnotationUtils;
import ac.uk.susx.tag.writer.OutputWriter;
import ac.uk.susx.tag.writer.StringWriter;

public class StringBagOfWordsOutputDocumentFormatter implements OutputDocumentFormatter<String,String>{
	
	private final char TOKEN_DELIM;
	private final StringTokenAnnotatorFormatter tokenMaker;

	public StringBagOfWordsOutputDocumentFormatter(){
		this('\t');
	}
	
	public StringBagOfWordsOutputDocumentFormatter(char delimiter){
		TOKEN_DELIM = delimiter;
		tokenMaker = new StringTokenAnnotatorFormatter();
	}

	public void processDocument(String outputFileName, Map<Class<? extends Annotator>, Map<IndexToken, Annotation<String>>> collection) {
		StringWriter docWriter = null;
		try {
			docWriter = new StringWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<IndexToken, Collection<Annotation<String>>> collatedAnnotations = AnnotationUtils.collateAnnotations(collection);
		
		
		for(Collection<Annotation<String>> annotations : collatedAnnotations.values()){
			docWriter.writeToken(tokenMaker.createToken(annotations));
			docWriter.writeToken(String.valueOf(TOKEN_DELIM));
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSubDocument(OutputWriter<String> writer, Map<Class<? extends Annotator>, Map<IndexToken, Annotation<String>>> collection) {
		
		Map<IndexToken, Collection<Annotation<String>>> collatedAnnotations = AnnotationUtils.collateAnnotations(collection);
		
		for(Collection<Annotation<String>> annotations : collatedAnnotations.values()){
			try {
				writer.writeToken(tokenMaker.createToken(annotations));
				writer.writeToken(String.valueOf(TOKEN_DELIM));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}

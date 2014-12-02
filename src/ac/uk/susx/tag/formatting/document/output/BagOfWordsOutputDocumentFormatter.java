package ac.uk.susx.tag.formatting.document.output;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.formatting.token.BasicTabSeperatedTokenFormatter;
import ac.uk.susx.tag.writer.IOutputWriter;
import ac.uk.susx.tag.writer.StandardOutputWriter;

public class BagOfWordsOutputDocumentFormatter implements IOutputDocumentFormatter {
	
	private final CharSequence TOKEN_DELIM;

	public BagOfWordsOutputDocumentFormatter(){
		this("\t");
	}
	
	public BagOfWordsOutputDocumentFormatter(CharSequence delimiter){
		TOKEN_DELIM = delimiter;
	}

	public void processDocument(String outputFileName, Document document) {
		BasicTabSeperatedTokenFormatter tokenMaker = new BasicTabSeperatedTokenFormatter();
		StandardOutputWriter docWriter = null;
		docWriter = new StandardOutputWriter(outputFileName);
		
		Iterator<Sentence> iter = document.iterator();
		while(iter.hasNext()) {
			Collection<List<? extends IAnnotation<?>>> groupedAnnotations = iter.next().getAllIndexedAnnotations();
			for(List<? extends IAnnotation<?>> annotations : groupedAnnotations){
				CharSequence token = tokenMaker.createToken(annotations);
				docWriter.writeToken(new StringBuilder().append(token).append(TOKEN_DELIM).toString());
			}
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSubDocument(IOutputWriter writer, Document document) {
		BasicTabSeperatedTokenFormatter tokenMaker = new BasicTabSeperatedTokenFormatter();
		Iterator<Sentence> iter = document.iterator();
		while(iter.hasNext()) {
			Collection<List<? extends IAnnotation<?>>> groupedAnnotations = iter.next().getAllIndexedAnnotations();
			for(List<? extends IAnnotation<?>> annotations : groupedAnnotations){
				CharSequence token = tokenMaker.createToken(annotations);
				try {
					writer.writeToken(new StringBuilder().append(token).append(TOKEN_DELIM).toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

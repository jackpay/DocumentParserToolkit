package ac.uk.susx.tag.formatting;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.indexing.IIndexToken;
import ac.uk.susx.tag.writer.IOutputWriter;
import ac.uk.susx.tag.writer.CharSequenceWriter;

public class BagOfWordsOutputDocumentFormatter implements IOutputDocumentFormatter<CharSequence>{
	
	private final CharSequence TOKEN_DELIM;

	public BagOfWordsOutputDocumentFormatter(){
		this("\t");
	}
	
	public BagOfWordsOutputDocumentFormatter(CharSequence delimiter){
		TOKEN_DELIM = delimiter;
	}

	public void processDocument(String outputFileName, Map<IIndexToken, List<IAnnotation<?>>> sortedCollection) {
		BasicTabSeperatedTokenFormatter tokenMaker = new BasicTabSeperatedTokenFormatter();
		CharSequenceWriter docWriter = null;
		try {
			docWriter = new CharSequenceWriter(outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(List<IAnnotation<?>> tokenColl : sortedCollection.values()){
			CharSequence token = tokenMaker.createToken(tokenColl);
			docWriter.writeToken(new StringBuilder().append(token).append(TOKEN_DELIM).toString());
		}
		try {
			docWriter.closeDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void processSubDocument(IOutputWriter<CharSequence> writer, Map<IIndexToken, List<IAnnotation<?>>> sortedCollection) {
		BasicTabSeperatedTokenFormatter tokenMaker = new BasicTabSeperatedTokenFormatter();
		for(List<IAnnotation<?>> tokenColl : sortedCollection.values()){
			CharSequence token = tokenMaker.createToken(tokenColl);
			try {
				writer.writeToken(new StringBuilder().append(token).append(TOKEN_DELIM).toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

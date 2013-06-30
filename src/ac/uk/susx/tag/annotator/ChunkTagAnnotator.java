package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.InvalidFormatException;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.GrammaticalAnnotation;
import ac.uk.susx.tag.configuration.GrammaticalConfiguration;
import ac.uk.susx.tag.document.Document;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;
import ac.uk.susx.tag.utils.ParserUtils;

public final class ChunkTagAnnotator implements Annotator<Document<String,String>, GrammaticalAnnotation, String, String>{

	private ChunkerME chunker;
	private static final String CHUNKSTART = "B-";
	private static final String INCHUNK = "I-";

	public void annotate(Document<String, String> doc)
			throws IncompatibleAnnotationException {
		annotate(doc,true);
	}

	public void annotate(Document<String, String> doc, boolean parseRawText)
			throws IncompatibleAnnotationException {
		Collection<Annotation<String>> chunktags = new ArrayList<Annotation<String>>();
		Collection<? extends Annotation<String>> sentences = doc.getAnnotations(GrammaticalConfiguration.AnnotatorTypes.SENTENCE.getAnnotator().getClass());
		if(sentences == null){
			GrammaticalConfiguration.AnnotatorTypes.SENTENCE.getAnnotator().annotate(doc);
		}
		sentences = doc.getAnnotations(GrammaticalConfiguration.AnnotatorTypes.SENTENCE.getAnnotator().getClass());
		chunktags.addAll(annotate(sentences));
		doc.addAnnotations(this.getClass(), chunktags);
	}

	public Collection<GrammaticalAnnotation> annotate(
			Collection<? extends Annotation<String>> sentences)
			throws IncompatibleAnnotationException {
		
		ArrayList<GrammaticalAnnotation> annotations = new ArrayList<GrammaticalAnnotation>();
		for(Annotation<String> sentence : sentences){
			annotations.addAll(annotate(sentence));
		}
		return annotations;
	}

	public synchronized Collection<GrammaticalAnnotation> annotate(
			Annotation<String> sentence)
			throws IncompatibleAnnotationException {
		ArrayList<GrammaticalAnnotation> annotations = new ArrayList<GrammaticalAnnotation>();
		Collection<? extends Annotation<String>> tokens = GrammaticalConfiguration.AnnotatorTypes.TOKEN.getAnnotator().annotate(sentence);
		Collection<? extends Annotation<String>> postags = GrammaticalConfiguration.AnnotatorTypes.POSTAG.getAnnotator().annotate(sentence);
		String[] strToks = ParserUtils.annotationsToArray(tokens, new String[tokens.size()]);
		String[] strTags = ParserUtils.annotationsToArray(postags, new String[postags.size()]);
		String[] chunkTags = chunker.chunk(strToks, strTags);
		
		int begin = 0;
		for(int i = 0; i < chunkTags.length; i++){
			Pattern pattern = Pattern.compile(Pattern.quote(strToks[i]));
			Matcher matcher = pattern.matcher(sentence.getAnnotation());
			matcher.find(begin);
			String chunk = chunkTags[i].replace(INCHUNK, "");
			chunk = chunk.replace(CHUNKSTART, "");
			GrammaticalAnnotation annotation = new GrammaticalAnnotation(chunk, sentence.getStart()+matcher.start(), sentence.getStart()+matcher.end());
			annotations.add(annotation);
			begin = matcher.end();
		}
		return annotations;
	}
	
	public void startModel() {
		if(!modelStarted()){
			try {
				chunker = new ChunkerME(new ChunkerModel(this.getClass().getClassLoader().getResourceAsStream("enchunker.bin")));
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean modelStarted() {
		return chunker != null;
	}

}

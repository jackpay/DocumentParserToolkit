package ac.uk.susx.tag.annotator;

import is2.data.SentenceData09;
import is2.lemmatizer.Lemmatizer;
import is2.lemmatizer.Options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class LemmatiserAnnotator extends AbstractAnnotator<String,String> {

	private Lemmatizer lemmatizer;
	private Class<IAnnotatorFactory<String,String>> tokeniser;
	private static final String smModelLoc = "resources/lemmatizer-eng-4M-v36.mdl";
	private static final String lrgModelLoc = "resources/CoNLL2009-ST-English-ALL.anna-3.3.lemmatizer.model";
	
	public LemmatiserAnnotator(Class<IAnnotatorFactory<String,String>> tokeniser) {
		this.tokeniser = tokeniser;
	}

	public synchronized List<IAnnotation<String>> annotate(IAnnotation<String> sentence) throws IncompatibleAnnotationException {
		SentenceData09 sent = new SentenceData09();
		ArrayList<IAnnotation<String>> annotations =  new ArrayList<IAnnotation<String>>();
		ArrayList<String> forms = new ArrayList<String>();
		forms.add("<root>");
		List<? extends IAnnotation<String>> tokens = null;
		try {
			tokens = AnnotatorRegistry.getAnnotator(tokeniser).annotate(sentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(IAnnotation<String> token : tokens) {
			forms.add(token.getAnnotation());
		}
		sent.init(forms.toArray(new String[forms.size()]));
		lemmatizer.apply(sent);
		for(int i = 0; i < tokens.size(); i++) {
			StringAnnotation ann = new StringAnnotation(sent.plemmas[i+1], tokens.get(i).getStart(),tokens.get(i).getEnd());
			annotations.add(ann);
		}
		return annotations;
	}

	public void startModel() {
		if(!modelStarted()) {
			Options ops = null;
			try {
				ops = new Options(new String[]{"-model",lrgModelLoc});
			} catch (IOException e) {
				e.printStackTrace();
			}
			lemmatizer = new Lemmatizer(ops.modelName,false);
		}
	}

	public boolean modelStarted() {
		return lemmatizer != null;
	}

	public List<? extends IAnnotation<String>> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		List<IAnnotation<String>> annos = annotate(sentence.getSentence());
		sentence.addAnnotations(this.getClass(), annos);
		return annos;
	}

}
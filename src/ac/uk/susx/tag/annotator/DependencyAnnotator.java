package ac.uk.susx.tag.annotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uk.ac.susx.tag.dependencyparser.Parser;
import uk.ac.susx.tag.dependencyparser.datastructures.Token;
import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.TokenAnnotation;
import ac.uk.susx.tag.annotator.factory.IAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class DependencyAnnotator extends AbstractAnnotator<Token,String> {
	
	private Parser parser;
	private final Class<? extends IAnnotatorFactory<String,String>> tokeniser;
	private final Class<? extends IAnnotatorFactory<String,String>> postagger;

	public DependencyAnnotator(Class<? extends IAnnotatorFactory<String,String>> tokeniser, Class<? extends IAnnotatorFactory<String,String>> postagger) {
		this.tokeniser = tokeniser;
		this.postagger = postagger;
		startModel();
	}

	@Override
	public synchronized List<? extends TokenAnnotation> annotate(Sentence sentence) throws IncompatibleAnnotationException {
		if(!modelStarted()) {
			startModel();
		}
		uk.ac.susx.tag.dependencyparser.datastructures.Sentence parserSent = new uk.ac.susx.tag.dependencyparser.datastructures.Sentence();
		ArrayList<TokenAnnotation> annotations = new ArrayList<TokenAnnotation>();
		try {
			List<IAnnotation<String>> tokens = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(tokeniser).getClass());
			List<IAnnotation<String>> pos = sentence.getSentenceAnnotations((Class<? extends IAnnotator<String, ?>>) AnnotatorRegistry.getAnnotator(postagger).getClass());
			for(int i = 0; i < tokens.size(); i++) {
				parserSent.add(tokens.get(i).getAnnotation(),pos.get(i).getAnnotation());
			}
			parser.parseSentence(parserSent);
			Iterator<Token> iter = parserSent.iterator();
			int i = 0;
			while(iter.hasNext()) {
				annotations.add(new TokenAnnotation(iter.next(),tokens.get(i).getStart(),tokens.get(i).getEnd()));
				i++;
			}
		} catch (IllegalAnnotationStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sentence.addAnnotations(this.getClass(), annotations);
		return annotations;
	}

	@Override
	/**
	 * Needs to be a sentence.
	 */
	public List<? extends TokenAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		return null;
	}

	@Override
	public void startModel() {
		if(parser == null) {
			try {
				parser = Parser.parserWithPennPosAndStanfordDeprels();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean modelStarted() {
		return parser == null;
	}

}

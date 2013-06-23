package ac.uk.susx.tag.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.uk.susx.tag.annotation.annotations.Annotation;
import ac.uk.susx.tag.annotation.annotator.Annotator;

public class StringDocument extends AbstractDocument <String,String> {

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Annotator>, Collection<Annotation<String>>> annotations;

	@SuppressWarnings("rawtypes")
	public StringDocument(String rawDoc) {
		super(rawDoc);
		annotations = new HashMap<Class<? extends Annotator>, Collection<Annotation<String>>>();
	}

	@SuppressWarnings("rawtypes")
	public Collection<? extends Annotation<String>> getAnnotations(
			Class<? extends Annotator> cl) {
		return annotations.get(cl);
	}

	@SuppressWarnings("rawtypes")
	public Map<Class<? extends Annotator>, Collection<Annotation<String>>> getDocumentAnnotations() {
		return annotations;
	}

	@SuppressWarnings("rawtypes")
	public void addAnnotations(
			Class<? extends Annotator> cl,
			Collection<Annotation<String>> annotations) {
		if(this.annotations.get(cl) == null){
			this.annotations.put(cl, new ArrayList<Annotation<String>>());
			this.annotations.get(cl).addAll(annotations);
		}
		else{
			this.annotations.get(cl).addAll(annotations);
		}
	}

}

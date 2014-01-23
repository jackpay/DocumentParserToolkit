package ac.uk.susx.tag.filter;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

import ac.uk.susx.tag.annotator.IAnnotator;

public class StopWordFilter extends AbstractAnnotationFilter<String>{
	
	private final String[] stopwords;

	public StopWordFilter(Class<? extends IAnnotator> annotator) {
		super(annotator, true, true);
		stopwords = getStopWords();
	}

	@Override
	public boolean matchAnnotation(String annotation) {
		String lann = annotation.toLowerCase();
		for(String s : stopwords){
			if(s.equals(lann)){
				return true;
			}
		}
		return false;
	}
	
	private String[] getStopWords(){
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("stopwords.txt"), writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] strs = writer.toString().split("\n");
		return strs;
	}

}

package ac.uk.susx.tag.filter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Sets;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;

public class StopWordFilter extends AbstractAnnotationFilter<String>{
	
	private final HashSet<String> stopwords;

	public StopWordFilter(Class<? extends IAnnotator> annotator) {
		super(annotator, true, true);
		stopwords = getStopWords();
	}

	@Override
	public boolean matchAnnotation(IAnnotation<String> annotation) {
		String lann = annotation.getAnnotation().toLowerCase();
		if(stopwords.contains(lann)) {
			return true;
		}
		return false;
	}
	
	private HashSet<String> getStopWords(){
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("stopwords.txt"), writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String out = writer.toString().replace("\n\n", "\n");
		String[] strs = out.split("\n");
		return Sets.newHashSet(strs);
	}

}

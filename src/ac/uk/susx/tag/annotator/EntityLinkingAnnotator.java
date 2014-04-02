package ac.uk.susx.tag.annotator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class EntityLinkingAnnotator extends AbstractStringAnnotator {
	
	private HashSet<String> entities;
	private static final String TAG = "entity";
	private static final String DELIM = "_";
	
	public EntityLinkingAnnotator() {
		entities = new HashSet<String>();
	}

	public List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		String ann = annotation.getAnnotation();
		for(String entity : entities) {
			Pattern patt = Pattern.compile(Pattern.quote(entity));
			Matcher match = patt.matcher(ann);
			if(match.matches()){
				String strEntity = ann.substring(match.start(), match.end());
				strEntity = strEntity.replace("\\s* ", DELIM).replace("\t", DELIM) + DELIM + TAG;
				annotations.add(new StringAnnotation(strEntity,match.start(),match.end()));
			}
		}
		return annotations;
	}

	public void startModel() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(getClass().getClassLoader().getResource("entities.txt").getFile()));
			String line;
			try {
				line = br.readLine();
				while(line != null) {
					entities.add(line.replace("\n", ""));
					line = br.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean modelStarted() {
		return entities == null ? false : true;
	}

	
}

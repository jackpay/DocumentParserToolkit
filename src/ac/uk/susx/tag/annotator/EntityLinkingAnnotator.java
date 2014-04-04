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
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class EntityLinkingAnnotator extends AbstractStringAnnotator {
	
	private HashSet<List<? extends IAnnotation<String>>> entities;
	private static final String TAG = "entity";
	private static final String DELIM = "_";
	
	public EntityLinkingAnnotator() {
		entities = new HashSet<List<? extends IAnnotation<String>>>();
	}

	public List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		List<? extends IAnnotation<String>> ann = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(annotation);
		for(IAnnotation<String> a : ann) {
			if(entities.contains(a)) {
				
			}
		}
		for(List<? extends IAnnotation<String>> entity : entities) {
			if(ann.contains(entity) && entity.length() > 3) {
				System.out.println("FOUND: " + entity + " " + ann.indexOf(entity)  + " " + ann.substring(ann.indexOf(entity)));
				boolean found = true;
				int i = 0;
				while(i < ann.length() && found) {
					Pattern patt = Pattern.compile(entity);
					Matcher match = patt.matcher(ann.substring(i));
					if(match.matches()){
						String strEntity = ann.substring(match.start(), match.end());
						System.out.println(strEntity);
						strEntity = strEntity.replace("\\s* ", DELIM).replace("\t", DELIM) + DELIM + TAG;
						annotations.add(new StringAnnotation(strEntity,match.start(),match.end()));
						i = match.end();
					}
					else{
						found = false;
					}
				}
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
					try {
						entities.add(StringAnnotatorEnum.TOKEN.getAnnotator().annotate(new StringAnnotation(line.replace("\n", ""),0,0)));
					} catch (IncompatibleAnnotationException e) {
						e.printStackTrace();
					}
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

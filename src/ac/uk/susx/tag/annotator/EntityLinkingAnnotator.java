package ac.uk.susx.tag.annotator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ac.uk.susx.tag.annotation.IAnnotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.utils.IncompatibleAnnotationException;

public class EntityLinkingAnnotator extends AbstractStringAnnotator {
	
	private HashMap<IAnnotation<String>, List<List<? extends IAnnotation<String>>>> entities;
	private static final String TAG = "entity";
	private static final String DELIM = "_";
	
	public EntityLinkingAnnotator() {
		entities = new HashMap<IAnnotation<String>, List<List<? extends IAnnotation<String>>>>();
	}

	public List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		List<? extends IAnnotation<String>> ann = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(annotation);
		int i = 0;
		while(i < ann.size()) {
			List<List<? extends IAnnotation<String>>> anns = entities.get(ann.get(i));
			if(anns != null) {
				StringBuilder sb = new StringBuilder();
				sb.append(ann.get(i)+ DELIM);
				int offsetEnd = ann.get(i).getEnd();
				innerloop: // label the loop to ensure correct breaking.
				for(List<? extends IAnnotation<String>> annL : anns) {
					StringBuilder sb2 = new StringBuilder();
					boolean found = true;
					int j = 0;
					whileloop:
					while(found && j < annL.size()) {
						found = annL.get(j).getAnnotation().equals(ann.get(i+j).getAnnotation()) ? true : false;
						j++;
						sb2.append(annL.get(j) + DELIM);
						if(!found) {
							break whileloop;
						}
					}
					if(found) {
						sb.append(sb2.toString() + DELIM);
						offsetEnd = annL.get(j).getEnd();
						i += j;
						break innerloop;
					}
				}
				sb.append(TAG);
				annotations.add(new StringAnnotation(sb.toString(),ann.get(i).getStart(),offsetEnd));
			}
			else {
				i++;
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
						List<? extends IAnnotation<String>> entity = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(new StringAnnotation(line.replace("\n", ""),0,0));
						if(entities.get(entity.get(0)) == null) {
							entities.put(entity.get(0), new ArrayList<List<? extends IAnnotation<String>>>());
						}
						if(entity.size() > 1) {
							entities.get(entity.get(0)).add(entity.subList(1, entity.size()));
						}
						Collections.sort(entities.get(entity.get(0)), new ListSorter());
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

	private class ListSorter implements Comparator<List<? extends IAnnotation<String>>> {

		public int compare(List<? extends IAnnotation<String>> o1, List<? extends IAnnotation<String>> o2) {
			if(o1.size() == o2.size()) {
				return 0;
			}
			return o1.size() > o2.size() ? -1 : 1;
		}
		
	}
}

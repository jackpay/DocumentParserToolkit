package ac.uk.susx.tag.annotator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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

	public synchronized List<StringAnnotation> annotate(IAnnotation<String> annotation) throws IncompatibleAnnotationException {
		ArrayList<StringAnnotation> annotations = new ArrayList<StringAnnotation>();
		List<? extends IAnnotation<String>> document = StringAnnotatorEnum.TOKEN.getAnnotator().annotate(annotation);
		int i = 0;
		while(i < document.size()) {
			List<List<? extends IAnnotation<String>>> entityPossibilities = entities.get(document.get(i));
			if(entityPossibilities != null) {
				StringBuilder sb = new StringBuilder();
				if(document.get(i).getAnnotation() != null){
					sb.append(document.get(i).getAnnotation()).append(DELIM);
				}
				int offsetEnd = document.get(i).getEnd();
				boolean foundAny = false;
				innerloop: // label the loop to ensure correct breaking.
				for(List<? extends IAnnotation<String>> entityPoss : entityPossibilities) {
					StringBuilder sb2 = new StringBuilder();
					boolean found = true;
					int j = 0;
					whileloop:
					while(found && j < entityPoss.size()) {
						if((i+j)+1 <  document.size()) {
							if(!entityPoss.get(j).getAnnotation().equals(document.get((i+j)+1).getAnnotation())) {
								found = false;
							}
						}
						if(found && entityPoss.get(j).getAnnotation() != null) {
							sb2.append(entityPoss.get(j).getAnnotation()).append(DELIM);
						}
						else {
							break whileloop;
						}
						j++;
					}
					if(found) {
						sb.append(sb2.toString());
						offsetEnd = i+j < document.size() ? document.get(i+j).getEnd() : document.get(document.size()-1).getEnd();
						i += j;
						foundAny = true;
						break innerloop;
					}
				}
				sb.append(TAG);
				StringAnnotation sa = new StringAnnotation(sb.toString(),document.get(i).getStart(),offsetEnd+1);
				annotations.add(sa);
				System.out.println(sa.getAnnotation());
				if(!foundAny) {
					i++;
				}
			}
			else {
				i++;
			}
		}
		return annotations;
	}

	public void startModel() {
		if(!entities.isEmpty()){
			return;
		}
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(getClass().getClassLoader().getResource("entities.txt").getFile()),"UTF-8"));
			String line;
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
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public boolean modelStarted() {
		return entities.isEmpty() ? false : true;
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

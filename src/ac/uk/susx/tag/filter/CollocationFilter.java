package ac.uk.susx.tag.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotation.StringAnnotation;
import ac.uk.susx.tag.annotator.IAnnotator;
import ac.uk.susx.tag.document.Sentence;
import ac.uk.susx.tag.utils.IllegalAnnotationStorageException;

public class CollocationFilter implements IFilter<String> {
	
	private static final String DELIM = "_";
	private HashSet<String> stopWords;
	private Map<String, List<String>> colls;
	private Class<? extends IAnnotator<String,?>> annotator;
	private boolean removeToks;
	
	
	public CollocationFilter(String collsLoc, Class<? extends IAnnotator<String,?>> annotator, boolean removeToks) {
		stopWords = getStopWords();
		colls = getCollocations(new File(collsLoc));
		this.annotator = annotator;
		this.removeToks = removeToks;
	}

	@Override
	public List<Annotation<String>> filterList(List<Annotation<String>> list) {
		List<Annotation<String>> newAnns = new ArrayList<Annotation<String>>();
		for(Annotation<String> anno : list) {
			if(colls.containsKey(anno.getAnnotation())) {
				int i;
				boolean match = true;
				StringBuilder sb = new StringBuilder().append(anno.getAnnotation());
				for(i = 0; i < colls.get(anno.getAnnotation()).size(); i++) {
					if(list.indexOf(anno) + (i+1) >= list.size()) {
						match = false;
						break;
					}
					if(!list.get(list.indexOf(anno) + (i+1)).getAnnotation().equals(colls.get(anno.getAnnotation()).get(i)) && !stopWords.contains(list.get(list.indexOf(anno)+(i+1)))) {
						match = false;
						break;
					}
					else{
						sb.append("_").append(colls.get(anno.getAnnotation()).get(i));
					}
				}
				if(match) {
					newAnns.add(new StringAnnotation(sb.toString(),anno.getStart(),list.get(list.indexOf(anno) + (i)).getEnd()));
				}
			}
		}
		return newAnns;
	}

	@Override
	public Sentence filterSentence(Sentence sentence) {
		try {
			if(removeToks) {
				List<Annotation<String>> newAnns = new ArrayList<Annotation<String>>();
				List<Annotation<String>> list = sentence.getSentenceAnnotations(annotator);
				for(Annotation<String> anno : list) {
					if(colls.containsKey(anno.getAnnotation())) {
						int i;
						boolean match = true;
						List<Annotation<String>> matchList = new ArrayList<Annotation<String>>();
						matchList.add(anno);
						for(i = 0; i < colls.get(anno.getAnnotation()).size(); i++) {
							if(list.indexOf(anno) + (i+1) >= list.size()) {
								match = false;
								break;
							}
							if(!list.get(list.indexOf(anno) + (i+1)).getAnnotation().equals(colls.get(anno.getAnnotation()).get(i)) && !stopWords.contains(list.get(list.indexOf(anno)+(i+1)))) {
								match = false;
								break;
							}
							else{
								matchList.add(list.get(list.indexOf(anno)+1));
							}
						}
						if(match) {
							newAnns.add(collocation(matchList));
							for(Annotation<String> an : matchList) {
								sentence.removeAnnotation(an.getOffset());
							}
						}
					}
				}
				sentence.addAnnotations(annotator, newAnns);
			}
			else {
				sentence.addAnnotations(annotator, filterList(sentence.getSentenceAnnotations(annotator)));
			}
		} catch (IllegalAnnotationStorageException e) {
			e.printStackTrace();
		}
		return sentence;
	}
	
	public Map<String, List<String>> getCollocations(File colloc) {
		Map<String, List<String>> colls = Maps.newHashMap();
		try {
			BufferedReader br = new BufferedReader(new FileReader(colloc));
			String line = br.readLine();
			while(line != null) {
				String[] coll = line.split("\\s+");
				colls.put(coll[0], Arrays.asList(Arrays.copyOfRange(coll, 1, coll.length)));
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return colls;
		
	}
	
	private  HashSet<String> getStopWords(){
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("stopwords.txt"), writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String out = writer.toString().replace("\n\n", "\n");
		HashSet<String> words = new HashSet<String>();
		for(String str : out.split("\n")) {
			words.add(str.toLowerCase());
		}
		return words;
	}
	
	private StringAnnotation collocation(List<Annotation<String>> list) {
		StringBuilder sb = new StringBuilder();
		for(Annotation<String> anno : list) {
			sb.append(anno.getAnnotation()).append("_");
		}
		return new StringAnnotation(sb.substring(0, sb.length()-1),list.get(0).getStart(),list.get(list.size()-1).getEnd());
	}

}

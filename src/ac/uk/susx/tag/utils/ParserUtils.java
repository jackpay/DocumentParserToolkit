package ac.uk.susx.tag.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ac.uk.susx.tag.annotation.Annotation;
import ac.uk.susx.tag.annotator.Annotator;
import ac.uk.susx.tag.indexing.IndexToken;

public class ParserUtils {
	
	@SuppressWarnings("rawtypes")
	public static <A> HashMap<IndexToken, ArrayList<Annotation<A>>> collectAnnotations(Map<Class<? extends Annotator>, 
			Collection<Annotation<A>>> annotations, Class<? extends Annotator> head){
		HashMap<IndexToken, ArrayList<Annotation<A>>> collectedAnnotations = new HashMap<IndexToken, ArrayList<Annotation<A>>>(annotations.size()+((int)annotations.size()/4));
		if(head != null && annotations.containsKey(head)){
			for(Annotation<A> ann : annotations.get(head)){
				if(collectedAnnotations.get(ann.getOffset()) == null){
					collectedAnnotations.put(ann.getOffset(), new ArrayList<Annotation<A>>());
					collectedAnnotations.get(ann.getOffset()).add(ann);
				}
				else{
					collectedAnnotations.get(ann.getOffset()).add(ann);
				}
			}
		}
		Iterator iter = annotations.keySet().iterator();
		while(iter.hasNext()){
			Object next = iter.next();
			if(!next.equals(head)){
				for(Annotation<A> ann : annotations.get(next)){
					if(collectedAnnotations.get(ann.getOffset()) == null){
						collectedAnnotations.put(ann.getOffset(), new ArrayList<Annotation<A>>());
						collectedAnnotations.get(ann.getOffset()).add(ann);
					}
					else{
						collectedAnnotations.get(ann.getOffset()).add(ann);
					}
				}
			}
		}
		return collectedAnnotations;
	}
	
	public static ArrayList<File> getFiles(String fileLocation, String suffix) throws IOException{
		if(fileLocation == null){
			throw new IOException("File location is null");
		}
		if(suffix == null){
			throw new IOException("File suffix is null");
		}
		File file = new File(fileLocation);
		ArrayList<File> files = new ArrayList<File>();
		if(file.exists()){
			if(file.exists() && file.isDirectory() && !file.isHidden()) {
				File[] fileList = file.listFiles();
				for(File f : fileList){
					if(!f.isHidden()){
						files.addAll(getFiles(f.getAbsolutePath(),suffix));
					}
				}
			}
			else{
				if(file.exists() && file.isFile() && file.getName().endsWith(suffix)){
					files.add(file);
				}
				else{
					throw new IOException("File error. Check input path and files");
				}
			}
		}
		return files;
	}
	
	public static String readFileAsString(String fileLocation) throws IOException{
		return readFileAsString(new File(fileLocation));
	}
	
	public static String readFileAsString(File file) throws java.io.IOException{ 
		byte[] buffer = new byte[(int) file.length()]; 
		BufferedInputStream f = new BufferedInputStream(new FileInputStream(file)); 
		f.read(buffer); 
		f.close();
		return new String(buffer);
	}
	
	public static <A> A[] annotationsToArray(Collection<? extends Annotation<A>> annotations, A[] array){
		Iterator<? extends Annotation<A>> iter = annotations.iterator();
		int i = 0;
		while(iter.hasNext()){
			array[i] = (A) iter.next().getAnnotation();
			i++;
		}
		return (A[]) array;
	}
}

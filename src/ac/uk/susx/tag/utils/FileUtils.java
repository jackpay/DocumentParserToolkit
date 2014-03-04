package ac.uk.susx.tag.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileUtils {

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

	public static String createOutputDirectory(String startingLocation) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String folderName = dateFormat.format(date);
		File file = new File(startingLocation);
		if(file.isFile()){
			try {
				throw new Exception("Output location must be a directory.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		folderName = FileUtils.enumerateFileNumber(startingLocation + "/" + folderName.replace("/", "-"),0);
		boolean success = new File(folderName).mkdirs();
		if(success){
			System.err.println("Output can be found in this location: " + folderName);
			return folderName;
		}
		else{
			try {
				throw new Exception("Directory could not be created in specified location.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static String enumerateFileNumber(String currentFolder, int currIt) {
		File file;
		if(currIt > 0){
			file = new File(currentFolder + "-(" + currIt + ")");
		}
		else{
			file = new File(currentFolder);
			if(!file.exists()){
				return currentFolder;
			}
			else{
				currIt++;
				return enumerateFileNumber(currentFolder,currIt);
			}
		}
		if(file.exists()){
			currIt++;
			return enumerateFileNumber(currentFolder, currIt);
		}
		return currentFolder + "-(" + currIt + ")";
	}

}

package ac.uk.susx.tag.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

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

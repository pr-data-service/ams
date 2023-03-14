package com.drps.ams.util;

import java.io.File;
import java.net.URI;

public class HelperService {
	
	//C:/Users/002ZX2744/Desktop/TEMP/pdf
	public static String getPath(String strPath, String fileName) {
		String fileFullPath = null;
		File file = new File(URI.create(strPath));
		if(file.isDirectory()) {
			//by pass
		} else if(file.mkdirs()) {
			fileFullPath = strPath + "/" +fileName;
		}
		
		
		int count = 1;
		file = new File(URI.create(fileFullPath));
		if(file.exists()) {
			fileFullPath += "_" + count++;
		}
		
		return fileFullPath;
	}
	
	public String manageDuplicateFileName(String fileFullPath) {
		return "";
	}

}

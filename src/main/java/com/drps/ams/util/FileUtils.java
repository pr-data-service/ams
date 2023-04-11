package com.drps.ams.util;

import java.io.File;
import java.io.IOException;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentEntity;

public class FileUtils {
	public static void createFolder (String path, String folderName) {
			
			File newDirectory = new File (path + "/" + folderName);
			if(!newDirectory.exists()) {
				Boolean created = newDirectory.mkdir();
				if (created) {
					System.out.println("Directory created successfully");
				}
				else {
					System.out.println("Failed to create directory");
				}
			}
			else {
				System.out.println("Directory already exist");
			}
	}
	
	public static void createFile (String path,String fileName) {
		try {
			File newFile = new File(path + fileName);
			if (newFile.createNewFile()) {
				System.out.println("File created " + newFile.getName());
			}
			else {
				System.out.println("File already exist");
			}
		} catch(IOException e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	}
	
	public static void deleteFile (String path) {
		try {
			File file = new File(path);
			if (file.delete()) {
				System.out.println("File deleted " + file.getName());
			}
			else {
				System.out.println("File doesnot exist");
			}
		} catch(Exception e) {
			System.out.println("An error occured");
			e.printStackTrace();
		}
	}
	
	public static void createDir(String path) {
		String osName = System.getProperty("os.name");
		System.out.println("OS Name: " + osName);
		
		String [] folderNames = path.split("/");
		String tempPath = "";
		for (int i = 0; i < folderNames.length; i++) {
			if(i == 0) {
				tempPath = folderNames[i].length() == 0 ? "/" : folderNames[i];
				
			} else {
				createFolder(tempPath, folderNames[i]);
				tempPath = tempPath + "/"+folderNames[i];
			}
		}
	}
	
	public static String prepairFilePath(String path, PaymentEntity entity, FlatDetailsEntity flatDetailsEntity ){
		
		createDir(path);
		
		// Month wise folder creation.....
		String monthYear = DateUtils.dateToString(entity.getPaymentDate(), "MM-yyyy");
		createFolder(path, monthYear);
		path = path + "/" + monthYear;
		
		String fileName = "payment-receipt_"+flatDetailsEntity.getFlatNo().replaceAll("/", "-")+"_"+ monthYear;
		if (entity.getIsCanceled() != null && entity.getIsCanceled()) {
			deleteFile(path + "/" +fileName + ".pdf");
			fileName = fileName + "_canceled";
		}
		path = path + "/" + fileName + ".pdf";
		return path;
	}
}

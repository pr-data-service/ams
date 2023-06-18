package com.drps.ams.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.exception.FileStorageException;

public class FileUtils {
	
	@Deprecated
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
	
	@Deprecated
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
	
	@Deprecated
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
	
	public static String prepairFilePathForPaymentReceipt(UserContext userContext, String path, PaymentEntity entity, FlatDetailsEntity flatDetailsEntity ){
		
		
		// Month wise dir creation.....
		String monthYear = DateUtils.dateToString(entity.getPaymentDate(), "MM-yyyy");
		path = path + "/" + monthYear;
		
		Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
		
		String fileName = "payment-receipt_"+ monthYear+"_"+entity.getBillNo();
		if (entity.getIsCanceled() != null && entity.getIsCanceled()) {
			deleteFile(path + "/" +fileName + ".pdf");
			fileName = fileName + "_canceled";
		}
		path = path + "/" + fileName + ".pdf";
		return path;
	}
	
	public static String prepairFilePathForVouchar(UserContext userContext, String path, ExpensesEntity entity){
		
		path = path + "/" + getApplicationBaseFilePath(userContext, path);
		
		// Month wise dir creation.....
		String monthYear = DateUtils.dateToString(entity.getExpenseDate(), "MM-yyyy");
		path = path + "/" + monthYear;
		
		Path fileStorageLocation = Paths.get(path).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
		
		String fileName = "vouchar_"+ monthYear+"_"+entity.getVoucherNo();
		if (entity.getIsCanceled() != null && entity.getIsCanceled()) {
			deleteFile(path + "/" +fileName + ".pdf");
			fileName = fileName + "_canceled";
		}
		path = path + "/" + fileName + ".pdf";
		return path;
	}
	
	public static String getApplicationBaseFilePath(UserContext userContext, String rootPath) {
		// dir for Apartment
		Long aprtmentId = userContext.getApartmentId();
		
		// dir for Session
		String sessionName = userContext.getSessionDetailsEntity().getName();
		String path = "aprt_" + aprtmentId + "/" + sessionName;
		if(rootPath != null && !StringUtils.isBlank(rootPath)) {
			path = rootPath.trim() + "/" + path;
		}
		return path;
	}
}

package com.drps.ams.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.drps.ams.bean.UserContext;

public class ZipFileUtils {
    public static File createZip(UserContext userContext, String path, String folderName, String namePrefix) throws Exception {
		
        String folderPath = path+"/"+folderName;
        String zipFilePath = path+"/"+(namePrefix == null ? "" : namePrefix+"-")+folderName+".zip";
    	
    	
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        
        zipFolder(folderPath, folderPath, zos);
        
        zos.close();
        fos.close();
        
        System.out.println("Folder successfully compressed to zip file.");
        File file = new File(zipFilePath);
        
        return file;
    }
    
    public static void zipFolder(String folderPath, String basePath, ZipOutputStream zos) throws Exception {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        
        for (File file : files) {
            if (file.isDirectory()) {
                zipFolder(file.getAbsolutePath(), basePath, zos);
            } else {
                String relativePath = file.getAbsolutePath().substring(basePath.length() + 1);
                ZipEntry ze = new ZipEntry(relativePath);
                zos.putNextEntry(ze);
                
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                
                fis.close();
            }
        }
    }
}

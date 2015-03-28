package com.java.io;
/*
* JavaIO: API for Sample Java IO Persistence services.
* 
* Copyright (C) 2014  Samuel T. C. Santos
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;


/**
 *  Class for manipulating files in Java.
 * 
 * - read/write/move/rename file
 * - create/delete/move/rename folder
 * - split path/get segments 
 * 
 * @author Samuel T. C. Santos
 *
 * @version  1.0 
 */
public class JavaIO {
	
	/**
	 * User Directory
	 */
	public static final String USER_DIR = System.getProperty("user.dir");
	/**
	 * file separator '\' ou '/'
	 */
	public static final String SEPARATOR = System.getProperty("file.separator");
	/**
	 * Row separator '\n'
	 */
	public static final String NEWLINE = System.getProperty("line.separator");
	/**
	 * Check the name of the Operating System.
	 */
	public static final String OPERATING_SYSTEM = System.getProperty("os.name");
	
	/**
	 * 
	 * Creates a directory within your project workspace.
	 * 
	 * workpace/myProject/folderName
	 * 
	 * @param folderName
	 */
	public static void createLocalFolder(String folderName){
		createFolder(USER_DIR + SEPARATOR + folderName);
	}
	
	/**
	 * Removes a directory from within the current project in the workspace.
	 * 
	 * @param folderName String
	 */
	public static void deleteLocalFolder(String folderName){
		deleteFolder(USER_DIR + SEPARATOR + folderName);
	}
	
	/**
	 * Creates a folder in any location on your computer.
	 * 
	 * @param path
	 *  example path "c:/tests/io"
	 */
	public static void createFolder(String path){
		if (!exist(path)){
			File folder = new File(path);
			folder.mkdirs();
		}
	}

	/**
	 * Removes a folder anywhere on the computer.
	 * 
	 * @param path
	 *  example de path "c:/tests/io"
	 */
	public static void deleteFolder(String path) {
		if (exist(path)){
			deleteAll(path);
		}
	}

	/**
	 * Delete all files in the directory.
	 * 
	 * @param path
	 *       the path of a directory system.
	 */
	private static void deleteAll(String path) {
		deleteAll(new File(path));
	}

	/**
	 * Delete directories recursively, subfolder and files.
	 * 
	 * @param file
	 *        the object of type File
	 */
	private static void deleteAll(File file) {
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (File f : files)
				deleteAll(f);
			file.delete();
		}
	}
	
	/**
	 * Check whether a file or directory exists.
	 * 
	 * @param path
	 *  example de path "c:/tests/io"
	
	 * @return
	 *  true ou false */
	public static boolean exist(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	/**
	 * Create a text file in the location specified by checking 
	 * if the place exists, if not exists it will be created.
	 * 
	 * @param path
	 * 	example "c:/tests/io"
	 * @param fileName
	 *  example file.txt
	 * @param content
	 *  
	 * @param overwrite boolean
	 */
	public static void createTextFile(String path, String fileName, String content, boolean overwrite){
		if( !exist(path)){
			createFolder(path);
		}
		saveFile(path+SEPARATOR+fileName, content, overwrite);
	}
	
	/**
	 *Save the data to disk, allowing you to choose to overwrite the contents or not.
	 * 
	 * @param fileName
	 * @param content
	 * @param overwrite
	 *  	true or false - tells whether the file should be overwritten.
	 */
	private static void saveFile(String fileName, String content, boolean overwrite) {
		FileOutputStream output = null;
	    try {
	      output = new FileOutputStream(new File(fileName), overwrite);
	      output.write(content.getBytes());
	      output.close();
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    } finally {
	      if (output != null) {
	        try {
	          output.close();
	        } catch (IOException e) {
	          System.err.println(e.getMessage()); 
	        }
	      }
	    }
	  } 
	
	/**
	 * Opens the file in the path informed.
	 * 
	 * @param path
	 *  example "c:/tests/io/file.txt"
	 *  
	 * @return String
	 */
	public static String openTextFile(String path) {
		  
		  String returnValue = "";
		  FileReader file = null;
		  
		  try {
		    file = new FileReader(path);
		    BufferedReader reader = new BufferedReader(file);
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		      returnValue += line + "\n";
		    }
		    reader.close();
		  } catch (Exception e) {
		      throw new RuntimeException(e);
		  } finally {
		    if (file != null) {
		      try {
		        file.close();
		      } catch (IOException e) {
		        System.err.println(e.getMessage());
		      }
		    }
		  }
		  return returnValue;
	}
	/**
	 * Creates an xml file and saved to disk.
	 * 
	 * @param path
	 * 	example "c:/tests/io/file.xml"
	 * @param fileName
	 *  example file.xml
	 * @param content
	 * @param overwrite
	 *   state whether or not to overwrite the contents.
	 */
	public static void createXMLFile(String path, String fileName, String content, boolean overwrite) {
		if(!exist(path)){
			createFolder(path);
		}
		saveFile(path+SEPARATOR+fileName, content, overwrite);
	} 
	
	/**
	 *O pens an xml file and returns the contents.
	 * 
	 * @param path
	 *  example "c:/tests/io/file.xml"
	
	 * @return String
	 */
	public static String openXMLFile(String path){
		return openTextFile(path);
	}

	/**
	 * Saves an object entered in the xml file format.
	 * 
	 * @param path
	 *  example "c:/tests/io/"
	 * @param fileName
	 *  example file.xml
	 * @param object
	 *   a java object
	 * @param overwrite
	 *   confirmation to overwrite
	 */
	public static void saveObjectToXML(String path, String fileName,Object object, boolean overwrite) {
		XStream xstream = new XStream(); 
		String xml = xstream.toXML(object);
		createXMLFile(path, fileName, xml, overwrite);
	}

	/**
	 * Retrieves an object from the xml file.
	 * 
	 * @param path
	 *  example "c:/tests/io/file.xml"
	
	 * @return
	 *  a Object */
	public static Object getObjectFromXML(String path) {
		XStream xstream = new XStream(); 
		String xml = JavaIO.openXMLFile(path);
		return xstream.fromXML(xml);
	}
	
	/** 
    *  Copy a file from one location to another.
    * 
    * @param origem
	*  origin path
    * @param destino 
    *  destiny path 
    * @param overwrite 
    * 	confirmation to overwrite
    * 
   
    * @throws IOException  */  
   public static void copy(File origem, File destino,boolean overwrite) throws IOException{  
      if (destino.exists() && !overwrite){  
         return;  
      }  
      if (destino.isFile() && !destino.exists()){
    	  destino.createNewFile();
      }
      if (destino.isDirectory() && !destino.exists()){
    	  destino.mkdirs();
      }
      FileInputStream   fisOrigem = new FileInputStream(origem);  
      FileOutputStream fisDestino = new FileOutputStream(destino);  
      FileChannel fcOrigem = fisOrigem.getChannel();    
      FileChannel fcDestino = fisDestino.getChannel();    
      fcOrigem.transferTo(0, fcOrigem.size(), fcDestino);    
      fisOrigem.close();    
      fisDestino.close();  
   }  
   
   /**
	 * Do a copy from a file.
	 * 
	 * @param reader
	 * @param writer
	
	 * @throws IOException  */
	private static void copy(Reader reader , Writer writer) throws IOException{
		BufferedReader bufReader = new BufferedReader(reader);
		String line;
		while( null != (line = bufReader.readLine())){
			writer.write(line + '\n');			
		}	
		writer.flush();
		writer.close();
		bufReader.close();
	}
	/**
	 * Copy a file stating only the Paths of origin and destination.
	 * 
	 * Use this method when you are sure that the destination exists!
	 * 
	 * @param originPath
	 * 	origin file path.
	 * @param destinyPath
	 *  destiny file path.
	
	 * @throws IOException  */
	public static void copy(String originPath, String destinyPath) throws IOException{
		InputStream in = new FileInputStream(originPath);
		OutputStream out = new FileOutputStream(destinyPath,false);
		copy(new InputStreamReader(in), new  OutputStreamWriter(out));
	}
	
	/**
	 * Copy a file stating only the Paths of origin and destination.
	 * 
	 * If destination folder does not exist, will be created!
	 * 
	 * @param originPath
	 * @param destinationPath
	 * @param fileName
	
	 * @throws IOException  */
	public static void copy(String originPath, String destinationPath, String fileName) throws IOException{
		if (!exist(destinationPath)){
			createFolder(destinationPath);
		}
		copy(originPath+SEPARATOR+fileName, destinationPath+SEPARATOR+fileName);
	}
     
   /** 
    * Copy all files from one directory to another.
    * 
    * @param origem
    *  origin directory.
    * @param destino
    *  destiny directory. 
    * @param overwrite
    *  confirmation to overwrite.
    *  
   
    * @throws IOException  */  
   public static void copyAll(File origem,File destino,boolean overwrite) throws IOException{  
      if (!destino.exists())  
         destino.mkdir();  
      if (!origem.isDirectory())  
         throw new UnsupportedOperationException("Directory [" + origem.getParent() + "] not found!");
     
	if (!destino.isDirectory() || !destino.exists())
		 throw new UnsupportedOperationException("Directory [" + destino.getParent() + "] not found!");

	 File[] files = origem.listFiles();
	
      for (File file : files) {  
         if (file.isDirectory())  
            copyAll(file,new File(destino+SEPARATOR+file.getName()),overwrite);  
         else{  
            copy(file, new File(destino+SEPARATOR+file.getName()),overwrite);  
         }  
      }  
   }

   /**
    * Rename a file
    * 
    * @param path
    *  file path
    * @param filename
    *  old file name
    * @param newName
    *  new file name
    */
	public static void renameFile(String path,String filename, String newName) {
		if (exist(path+SEPARATOR+filename)){
			File file = new File(path+SEPARATOR+filename);
			file.renameTo(new File(path+SEPARATOR+newName));
		}
	}

	/**
	 * Deleting a file.
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		if (exist(path)){
			File file = new File(path);
			if (file.isFile())
				file.delete();
		}
	}

	/**
	 * Move a file from origin to destiny.
	 * 
	 * @param pathOrigin
	 *   example "c:/tests/io"
	 * @param pathDestiny
	 *  example "c:/tests/svn"
	 * @param fileName
	 *  example "file.xml"
	
	 * @throws IOException
	 *   File not found or permission denied access. */
	public static void moveFile(String pathOrigin, String pathDestiny, String fileName) throws IOException {
		File origin = new File(pathOrigin + SEPARATOR + fileName);
		if (!exist(pathDestiny)){
			createFolder(pathDestiny);
		}
		File destiny = new File(pathDestiny + SEPARATOR + fileName);
		copy(origin, destiny, true);
		origin.delete();
	}

	/**
	 * Check if the operating system is Windows.
	 * 
	
	 * @return
	 *  true or false */
	public static boolean isWindows() {
		String os = OPERATING_SYSTEM.toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	/**
	 * Check if the operating system is Unix.
	 * 
	
	 * @return
	 *  true or false */
	public static boolean isUnix() {
		String os = OPERATING_SYSTEM.toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
	}
	/**
	 * Check if the operating system is Mac.
	 * 
	
	 * @return
	 *  true or false */
	public static boolean isMac() {
		String os = OPERATING_SYSTEM.toLowerCase();
		return (os.indexOf("mac") >= 0);
	}
	
	/**
	 * Check if the operating system is Solaris.
	 * 
	
	 * @return
	 *  true or false */
	public static boolean isSolaris() {
		String os = OPERATING_SYSTEM.toLowerCase();
		return (os.indexOf("sunos") >= 0);
	}

	
	/**
	 * Divides the path into segments.
	 * 
	 * @param path
	
	 * @return String[]
	 */
	public static String[] getSegments(String path) {
		if (path.contains("/")){
			return path.split("/");
		}
		String pattern = Pattern.quote(SEPARATOR);
		return path.split(pattern);
	}

	/**
	 * Getting the last segment.
	 * 
	 * @param path
	
	 * @return String
	 */
	public static String getLastSegment(String path) {
		String [] segments = getSegments(path);
		return segments[segments.length-1];
	}

	/**
	 * Remo
	 * @param path
	
	 * @return String
	 */
	public static String removeLastSegment(String path) {
		String last = getLastSegment(path);
		return path.replace(last, "");		
	}

	/**
	 * Listing all files in a folder.
	 * 
	 * Non-recursively.
	 * 
	 * @param path
	 * 
	
	 * @return File[]
	 */
	public static File[] list(String path) {
		File f = new File(path);
		return f.listFiles();
	}  
	

	/**
	 * Method main.
	 * @param args String[]
	 */
	public static void main(String[] args) {
		System.out.println("JavaIO version 1.0.4");
		JavaIO j = new JavaIO();
		Object obj = j.getObjectFromXML("coveragePriorJ.xml");
		System.out.println(obj);
	}
}

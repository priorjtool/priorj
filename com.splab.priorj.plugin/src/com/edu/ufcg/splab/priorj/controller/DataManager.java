package com.edu.ufcg.splab.priorj.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.java.io.JavaIO;

import coverage.TestCase;
import coverage.TestSuite;

/**
 * Data Manager class.
 *  
 * @author Samuel T. C. Santos
 *
 */
public class DataManager {
	
	private static String localbase;
	private static String projectFolder;
	private static String versionFolder;
	private static final String SLASH = JavaIO.SEPARATOR;
		
	/**
	 * Creating the local base folder.
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static void createLocalbase(String path) throws Exception {
		if (path != null && !path.isEmpty()){
			DataManager.localbase = path;
			JavaIO.createFolder(path);
		}
		else{
			throw new Exception("Invalid Path!");
		}
	}
	
	/**
	 * This method save Coverage Data to XML file.
	 * 
	 * @param localPath
	 * @param fileName
	 * @param allSuites
	 */
	public static void saveCoverageData(String localPath, String fileName,	@SuppressWarnings("rawtypes") List<List> allSuites) {
		JavaIO.saveObjectToXML(localPath, fileName, allSuites, false);
	}
	/**
	 * Opening the coverage file and retrieve coverage data.
	 * 
	 * @param filePath
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<List> openCoverageData(String filePath) {
		@SuppressWarnings("unchecked")
		List<List> coverage = (List<List>) JavaIO.getObjectFromXML(filePath);
		return coverage;
	}
	
	/**
	 * Opening the coverage file and retrieve coverage data in the current local.
	 */
	@SuppressWarnings("rawtypes")
	public static List<List> openCoverageData(){
		// TODO MUDAR AQUI.
		String path = DataManager.getCurrentPath()+SLASH+"coveragePriorJ.xml";
		return openCoverageData(path);
	}
	
	public static List<String> getAllFileNamesStartingWith(final String path, final String matcher) {
		List<String> results = new ArrayList<String>();


		File[] files = new File(path).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isFile() && file.getName().startsWith(matcher)) {
		        results.add(file.getName());
		    }
		}
		
		return results;
	}
	
	public static List<String> getSuiteList(final String path) {
		List<String> results = new ArrayList<String>();


		File[] files = new File(path).listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 

		for (File file : files) {
		    if (file.isDirectory() && !file.getName().equals("js") && !file.getName().equals("fonts") && !file.getName().equals("css")) {
		        results.add(file.getName());
		    }
		}
		
		return results;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<TestSuite> openAllCoverageData(){
		List<String> suiteNames = getSuiteList(DataManager.getCurrentPath() + SLASH);
		List<TestSuite> suites = new ArrayList<TestSuite>();
		for (String name : suiteNames) {
			List<String> coverageFileNames = getAllFileNamesStartingWith(DataManager.getCurrentPath() + SLASH + name + SLASH, "coveragePriorJ");
			String[] split = name.split("-");
			TestSuite t = new TestSuite(split[0], split[1]);
			List<TestCase> all = new ArrayList<TestCase>();
			for (String fileName : coverageFileNames) {
				String path = DataManager.getCurrentPath() + SLASH + name + SLASH + fileName;
				@SuppressWarnings("unchecked")
				List<TestCase> coverage = (List<TestCase>) JavaIO.getObjectFromXML(path);
				System.out.println(coverage);
				all.addAll(coverage);
			}
			t.setTestCases(all);
			suites.add(t);
		}
		return suites;
//		String path = DataManager.getCurrentPath()+SLASH+"coveragePriorJ.xml";
//		
//		return openCoverageData(path);
	}
	
	/**
	 * Create a sub folder to save prioritized version to same project.
	 * 
	 * @param projectFolder
	 * @param versionFolder
	 * @throws Exception 
	 */
	public static void createFolderVersion(String projectFolder, String versionFolder) throws Exception {
		if (DataManager.localbase.isEmpty())
			throw new Exception("Set local base path!");
		if ( validate(projectFolder) && validate(versionFolder)){
			DataManager.projectFolder = projectFolder;
			DataManager.versionFolder  = versionFolder;
			JavaIO.createFolder(DataManager.getCurrentPath());
		}
	}
	
	
	/**
	 * This method create a project folder inside the local base.
	 * 
	 * @throws Exception
	 */
	public static void createProjectFolder(String folderName) throws Exception{
		if(DataManager.localbase.isEmpty())
			throw new Exception("Set local base path!");
		DataManager.projectFolder = folderName;
		JavaIO.createFolder(DataManager.localbase+SLASH+folderName);
	}
	/**
	 * Get the project name.
	 * 
	 * @return
	 */
	public static String getProjectFolderName(){
		return DataManager.projectFolder;
	}
	
	/**
	 * Return the version name.
	 * 
	 * @return
	 */
	public static String getProjectVersion(){
		return DataManager.versionFolder;
	}
	
	/**
	 * Get the location where the artifacts are saved.
	 * 
	 * @return
	 */
	public static String getLocalBasePath() {
		return DataManager.localbase;
	}
	
	/**
	 * Basic validation to a system path!
	 * 
	 * @param path
	 * @return
	 */
	private static boolean validate(String path){
		return path != null && !path.isEmpty();
	}
	
	/**
	 * This method save 
	 * @param string
	 * @param report
	 */
	public static void save(String filename, String content) {
		JavaIO.createTextFile(DataManager.getCurrentPath(), filename, content, false);
	}
	/**
	 * Saving in a sub directory
	 * @param filename
	 * @param folder
	 * @param content
	 */
	public static void save(String filename, String folder, String content){
		JavaIO.createTextFile(DataManager.getCurrentPath()+SLASH+folder, filename, content, false);
	}

	public static String openFile(String filePath) {
		return JavaIO.openTextFile(filePath);
	}
	
	/**
	 * This method build a current path where data are saved.
	 * 
	 * 
	 * @return
	 *   localBase/project/version/
	 */
	public static String getCurrentPath(){
		return DataManager.localbase + SLASH + DataManager.projectFolder + SLASH + DataManager.versionFolder;
	}
	
	public static String traceRoutesFromBase(){
		StringBuilder trace= new StringBuilder();
		trace.append("var links = [];\n\n");
		String base = getLocalBasePath();
		File dir = new File(base);
		File [] files = dir.listFiles(); 
		for (File f : files){
			if (f.isDirectory()){
				File [] subDirs = f.listFiles();
				String line = "[";
				for (int i=0 ; i<subDirs.length; i++){
					if (subDirs[i].isDirectory()){
						line += "'"+subDirs[i].getName()+"'";
					}
					if (i<subDirs.length-1){
						line += ", ";
					}
				}
				line += "]";
				trace.append("links.push({\n");
				trace.append("\tname : '"+f.getName()+"',\n");
				trace.append("\tversions : "+ line+"\n");
				trace.append("});\n\n");
				
			}
		}
		return trace.toString();
	}
	
//	public static void main(String[] args) throws Exception {
//		DataManager.createLocalbase("C:/Users/xpto/base");
//		System.out.println(DataManager.traceRoutesFromBase());
//	}
}

package com.edu.ufcg.splab.priorj.coverage.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.edu.ufcg.splab.priorj.controller.DataManager;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;
import com.google.gson.Gson;

/**
 * <code>TestSuiteController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class TestSuiteController {

	private static TestSuiteController instance;
	
    public static TestSuiteController getInstance() {
    	instance = new TestSuiteController();
    	return instance;
    }
    
    /**
     * Add a testSuite object into the database.
     * 
     * @param testSuite object.
     * @throws IOException 
     */
    public void save(final List<TestSuite> testSuite, final String directoryName) throws IOException {
    	for (TestSuite suite : testSuite) {
			String filePath = directoryName + "/coveragePriorJ-" + suite.getPackageName() + "-" + suite.getName() + ".json";
			Gson gson = new Gson();
			String json = gson.toJson(suite);
			//write converted json data to a file named "file.json"
			System.out.println(json);
			FileWriter writer = new FileWriter(filePath);
			writer.write(json);
			writer.close();
    	}
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<TestSuite> with all objects presents in database.
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    public List<TestSuite> getAll() {
    	List<TestSuite> suites = new ArrayList<TestSuite>();
    	List<String> jsonFileNames = DataManager.getAllFileNamesStartingWith("C:/PriorJ/workspace/priorj/com.splab.priorj.plugin/database/", "coveragePriorJ-");
    	for (String fileName : jsonFileNames) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader("C:/PriorJ/workspace/priorj/com.splab.priorj.plugin/database/" + fileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Gson gson = new Gson();
            TestSuite suite = gson.fromJson(reader, TestSuite.class);
            suite.teste();
            suites.add(suite);
		}
    	return suites;
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<TestSuite> with all objects presents in database.
     * @throws IOException 
     * @throws UnsupportedEncodingException 
     */
    public List<TestSuite> getAllOriginal() {
    	List<TestSuite> suites = new ArrayList<TestSuite>();
    	List<String> jsonFileNames = DataManager.getAllFileNamesStartingWith("C:/PriorJ/workspace/priorj/com.splab.priorj.plugin/database-original/", "coveragePriorJ-");
    	for (String fileName : jsonFileNames) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader("C:/PriorJ/workspace/priorj/com.splab.priorj.plugin/database-original/" + fileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Gson gson = new Gson();
            TestSuite suite = gson.fromJson(reader, TestSuite.class);
            suite.teste();
            suites.add(suite);
		}
    	return suites;
    }
    
}

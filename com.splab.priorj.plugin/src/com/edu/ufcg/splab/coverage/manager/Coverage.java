package com.edu.ufcg.splab.coverage.manager;

import java.util.ArrayList;
import java.util.List;

import coverage.ClassCode;
import coverage.Method;
import coverage.Statement;
import coverage.TestCase;
import coverage.TestSuite;


/**
 * Manipulate objects in the model coverage.
 * 
 * @author Samuel T. C. Santos
 *
 */
public class Coverage {
	
	/**
	 * Get One List with all suites from Many Suite Lists
	 * 
	 * @param allSuite
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<TestSuite> getSuiteList(List<List> allSuite){
		List<TestSuite> all = new ArrayList<TestSuite>();
		for(List list : allSuite){
			all.addAll(list);
		}
		return all;
	}
	/**
	 * Get all class codes covered by a list of Test Suites.
	 * 
	 * @param suites
	 * @return
	 */
	public List<ClassCode> getClassCodeList(List<TestSuite> suites){
		List<ClassCode> classes = new ArrayList<ClassCode>();
		for (TestSuite suite: suites){
			for (TestCase test : suite.getTestCases()){
				classes.addAll(test.getClassCoverage());
			}
		}
		return classes;
	}
	/**
	 * Get all Test Cases in the Suite Lists.
	 * 
	 * @param suites
	 * @return
	 */
	public List<TestCase> getAllTests(List<TestSuite> suites) {
		List<TestCase> all = new ArrayList<TestCase>();
		for (TestSuite suite: suites){
    		for (TestCase test : suite.getTestCases()){
    			addNumberOfMethods(test);
    			addNumberOfStatements(test);
    			all.add(test);
    		}
    	}
		return all;
	}
	
	/**
	 * Get All Methods in the SUT.
	 * 
	 * @return
	 */
	public List<Method> getAllMethods(List<TestSuite> suites) {
		List<Method> all = new ArrayList<Method>();
		for (ClassCode classcode : getClassCodeList(suites)){
			all.addAll(classcode.getMethodCoverage());
		}
		return all;
	}
	
	/**
	 * Get all Statement in the SUT.
	 * 
	 * @param suites
	 * @return
	 */
	public List<Statement> getAllStatements(List<TestSuite> suites) {
		List<Statement> all = new ArrayList<Statement>();
		for (Method method : getAllMethods(suites)){
			all.addAll(method.getStatementCoverage());
		}
		return all;
	}
		
	/**
	 * Add number of methods covered by test.
	 * 
	 * @param test
	 */
	private void addNumberOfMethods(TestCase test) {
		int numberMethods = test.getNumberMethodsCoveredDistinct(); 
		test.setNumberMethodsCoveredDistinct(numberMethods);
	}
	/**
	 * Add number of statements covered by test.
	 * @param test
	 */
	private void addNumberOfStatements(TestCase test) {
		int numberStatements = test.getNumberStatementsCoverageDistinct();
		test.setNumberStatementsCoverageDistinct(numberStatements);
	}
	
	public static void main(String[] args) {
		System.out.println("Coverage version 1.0.6");
	}
}

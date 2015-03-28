package com.edu.ufcg.splab.coverage.dao;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos, Julio H. Rocha
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

import java.util.ArrayList;
import java.util.List;

import com.java.io.JavaIO;

import coverage.*;


/**
 * <code>Repository</code> The class repository enables data saved in XML format following 
 * the trail of the tests. is responsible for the construction of the log file.
 * 
 * @author Samuel Santos
 * @author Julio Henrique
 *
 */
public class Repository {

    private List<TestSuite> suites;
    private TestSuite atualSuite;
    private TestCase atualTestCase;
    private ClassCode atualClass;
    private Method atualMethod;
    //private Writer escritor;
    
    private String dir;
    private String file;
    
    /**
     * <code>Repository</code> Constructor.
     */
    public Repository() {
       this.suites = new ArrayList<TestSuite>();
//       init();
    }

	public void init() {
		dir = JavaIO.USER_DIR+JavaIO.SEPARATOR+"report"+JavaIO.SEPARATOR;
		file = dir + "coveragePriorJ.xml";

	   if(!JavaIO.exist(dir)){
		   JavaIO.createLocalFolder("report");
	   }
	   JavaIO.createXMLFile(dir, "coveragePriorJ.xml", "<list>", true);
	   
	}

    /**
     * Get the method that is currently running.
     * 
     * @return a object of type <code>Method</code> 
     */
    public Method getAtualMethod() {
        return atualMethod;
    }

    /**
     * Set the method that is currently running.
     * 
     * @param atualMethod a object of type <code>Method</code>.
     */
    public void setAtualMethod(Method atualMethod) {
        this.atualMethod = atualMethod;
    }

    /**
     * Get the list of suite test.
     *  
     * @return a list of <code>TestSuite</code>.
     */
    public List<TestSuite> getSuites() {
        return suites;
    }

    /**
     * Set the list of suite test.
     * 
     * @param suites a list of  <code>TestSuite</code>.
     */
    public void setSuites(List<TestSuite> suites) {
        this.suites = suites;
    }

    /**
     * Get the test suite currently running.
     * 
     * @return a object of type  <code>TestSuite</code>.
     */
    public TestSuite getAtualSuite() {
        return atualSuite;
    }

    /**
     * Set the test suite currently running.
     * 
     * @param atualSuite a object of type  <code>TestSuite</code>.
     */
    public void setAtualSuite(TestSuite atualSuite) {
        this.atualSuite = atualSuite;
    }

    /**
     * Get the test case currently running.
     * 
     * @return a object of type <code>TestCase</code>.
     */
    public TestCase getAtualTestCase() {
        return atualTestCase;
    }

    /**
     * Set the test case currently running.
     * 
     * @param atualTestCase a object of type <code>TestCasse</code>.
     */
    public void setAtualTestCase(TestCase atualTestCase) {
        this.atualTestCase = atualTestCase;
    }

    /**
     * Set the class code currently running.
     * 
     * @return a object of type <code>ClassCode</code>.
     */
    public ClassCode getAtualClass() {
        return atualClass;
    }

    /**
     * Set the class code currently running.
     * 
     * @param atualClass a object of type <code>ClassCode</code>.
     */
    public void setAtualClass(ClassCode atualClass) {
        this.atualClass = atualClass;
    }

    /**
     * Method which adds a <code>TestSuite</code> in the log.
     * 
     * @param packageName the package name.
     * @param name the test suite name.
     */
    private void addTestSuite(String packageName, String name) { 
        TestSuite suite = new TestSuite(packageName, name);
        if (getAtualSuite() == null) {
            setAtualSuite(suite);
        } else if (!getAtualSuite().toString().equals(suite.toString())) {
            suites.add(atualSuite);
            setAtualSuite(suite);
        }
    }

    /**
     * Method which adds a <code>TestCase</code> in the log.
     * 
     * @param packageName the package name.
     * @param suiteName the test suite name.
     * @param testCase the test case name.
     */
    public void addTestCase(String packageName, String suiteName, String testCase) {
        addTestSuite(packageName, suiteName);
        TestCase tc = new TestCase(testCase);
        
        tc.setSignature(packageName+"."+suiteName+"."+testCase);
        
        if (getAtualTestCase() == null) {
            setAtualTestCase(tc);
        } else if (!getAtualTestCase().toString().equals(tc.toString())) {
            atualSuite.addTestCase(atualTestCase);
            setAtualClass(null);
            setAtualMethod(null);
            setAtualTestCase(tc);
        }

    }
    /**
     * 
     * @param packageName
     * @param suiteName
     * @param testCase
     * @param signature
     */
    public void addTestCase(String packageName, String suiteName, String testCase, String signature) {
        addTestSuite(packageName, suiteName);
        TestCase tc = new TestCase(testCase);
        tc.setSignature(signature);
        if (getAtualTestCase() == null) {
            setAtualTestCase(tc);
        } else if (!getAtualTestCase().toString().equals(tc.toString())) {
            atualSuite.addTestCase(atualTestCase);
            setAtualClass(null);
            setAtualMethod(null);
            setAtualTestCase(tc);
        }

    }
    

    /**
     * Method which adds a <code>ClassCode</code> in the log.
     * 
     * @param packageName the package name.
     * @param name the class code name.
     */
    private void addClassCode(String packageName, String name) {
        ClassCode classCode = new ClassCode(packageName, name);
        if (getAtualTestCase() == null || classCode.toString().equals(getAtualClass())) {
            return;
        }
        setAtualClass(classCode);
        getAtualTestCase().addClassCoverage(getAtualClass());
    }

    /**
     * Method which adds a <code>Method</code> in the log.
     * 
     * @param packageName the package name.
     * @param className	 the class name.
     * @param name	the method name.
     */
    public void addMethod(String packageName, String className, String name) {
        Method method = new Method(name);
        addClassCode(packageName, className);
        if (getAtualClass() == null || method.toString().equals(getAtualMethod())) {
            return;
        }
        setAtualMethod(method);
        getAtualClass().addMethod(getAtualMethod());
    }

    /**
     * Method which adds a Statement in the log.
     * 
     * @param number a string representing the number line.
     */
    public void addStatement(String number) {
        Statement sttm = new Statement(number);
        if (getAtualMethod() != null) {
            getAtualMethod().addStatement(sttm);
        }
    }

    /**
     * Method that performs recording data in the XML file.
     */
    public void commit() {
        getAtualSuite().addTestCase(getAtualTestCase());
        suites.add(getAtualSuite());
        JavaIO.saveObjectToXML(dir, "coveragePriorJ.xml", suites,true);
    }
    
    /**
     * Ends the process of writing the XML file, thus completing the construction of the log file.
     */
    public void finishCommit() {
    	JavaIO.createXMLFile(dir, "coveragePriorJ.xml", "</list>",true);
    }
}

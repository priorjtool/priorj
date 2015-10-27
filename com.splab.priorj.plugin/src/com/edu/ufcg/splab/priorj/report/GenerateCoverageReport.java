package com.edu.ufcg.splab.priorj.report;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2014  SPLab
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

import com.edu.ufcg.splab.priorj.coverage.model.ClassCode;
import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;
import com.edu.ufcg.splab.priorj.coverage.model.TestCase;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;

/**
 * This class generate a simple coverage report.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 * 
 */
public class GenerateCoverageReport {
    
	private List<TestSuite> suites;
	private List<String> testCases;
	private List<String> classes;
	private List<String> methods;
	private List<String> statements;
	private List<String> affectedBlocks;
	
    /**
     * Constructor.
     * 
     * @param suites
     *      A list of Test suites.
     */
	public GenerateCoverageReport(final List<TestSuite> suites, final List<String> affectedBlocks){
		this.suites = suites;
		this.testCases = new ArrayList<String>();
		this.classes = new ArrayList<String>();
		this.methods = new ArrayList<String>();
		this.statements = new ArrayList<String>();
		this.affectedBlocks = affectedBlocks;
	}
	
    /**
     * Get the coverage report generated.
     * 
     * @return
     *      The coverage report.
     */
	public String generateCoverageReport(){
            addAllTestCases();
            StringBuilder builder = new StringBuilder();
            
            builder.append("var coverage = [];\n\n");
            builder.append("var coverageGlobal = {\n");
            builder.append("\tsuites: "+ getCountUniqueSuites() +",\n");
            builder.append("\ttestcases: "+testCases.size()+",\n");
            builder.append("\tclasses: "+classes.size()+",\n");
            builder.append("\tmethods: "+methods.size()+",\n");
            builder.append("\tstatements: "+statements.size()+",\n");
            builder.append("\tchangesCovered: [");
        	for (int i = 0; i < affectedBlocks.size(); i++) {
        		String change = affectedBlocks.get(i);
        		if(statements.contains(change)) {
        			builder.append("'" + change + "',");
        		}
			}
        	if(builder.charAt(builder.length()-1) == ',') {
        		builder.deleteCharAt(builder.length()-1);
        	}
        	builder.append("]");
            
            builder.append("\n};\n\n");
            
            builder.append("$(function(){\n");
            builder.append("\tloadData();\n");
            builder.append("});\n\n");
            
            builder.append("function loadData(){\n");
            
          for (TestSuite suite : suites) {
        	String suiteName = suite.toString();
          	for (TestCase testcase : suite.getTestCases()) {
          		builder.append("\tcoverage.push({\n");
          		builder.append("\t\ttestsuite  : '"+suiteName+"',\n");
          		builder.append("\t\ttestcase   : '"+testcase.getName()+"',\n");
          		builder.append("\t\tclasses    : "+getCountUniqueClassCoverage(testcase) + ",\n");
          		builder.append("\t\tmethods    : "+getCountUniqueMethodCoverage(testcase) + ",\n");
          		builder.append("\t\tstatements : "+getCountUniqueStatementCoverage(testcase) + ",\n");
          		
          		builder.append("\t\tchangesCovered : [");
//          		List<String> changes = this.getTestCaseChangesCovered(testcase);
          		for (String cc : testcase.getStatementsCoverage()) {
          			builder.append("'" + cc + "'" + ",");
				}
          		// Deleta a �ltima v�rgula.
          		if(!testcase.getStatementsCoverage().isEmpty()) {
          			builder.deleteCharAt(builder.length() - 1);
          		}
          		builder.append("]");
          		builder.append("\n\t});\n");
          	}
          }
          
          builder.append("}\n\n");
          
          
          builder.append("\tfunction getCoverageReportData(){\n");
          builder.append("\t\treturn coverage;\n");
          builder.append("\t}\n\n");

          builder.append("\tfunction getCoverageGlobalData(){\n");
          builder.append("\t\treturn coverageGlobal;\n");
          builder.append("\t}\n\n");
        	
          return builder.toString();          
	}
	
	/**
	 * Counting Unique Suites.
	 * 
	 * @return
	 */
	private int getCountUniqueSuites(){
		List<String> found = new ArrayList<String>();
		for (TestSuite suite : suites){
			if(!found.contains(suite.getName())){
				found.add(suite.getName());
			}
		}
		return found.size();
	}
	/**
	 * Adding all test cases.
	 * 
	 */
	private void addAllTestCases(){
		if(this.testCases.size() != 0) return;
		for (TestSuite suite : suites) {
			String nameSuite = suite.toString();
			List<TestCase> tcs = suite.getTestCases();
			for (TestCase testCase : tcs) {
				String str = nameSuite+"."+testCase.getName();
				this.testCases.add(str);
				addAllClassCovered(testCase);
			}
		}
	}
	
	private void addAllClassCovered(TestCase tc){
		for (ClassCode clazz : tc.getClassCoverage()) {
			String className = clazz.toString();
			if(!this.classes.contains(className)){
				this.classes.add(className);
			}
			addAllMethodsCoverage(clazz);
		}
	}
	
	private void addAllMethodsCoverage(ClassCode clazz) {
		for (Method method : clazz.getMethodCoverage()) {
			String methodName = clazz.toString()+"."+method.getName();
			if(!this.methods.contains(methodName)){
				this.methods.add(methodName);
			}
			addAllStatementCovered(methodName, method);
			
		}
	}

	private void addAllStatementCovered(String methodName, Method method) {
		for (Statement sttm : method.getStatementCoverage()) {
			String stmName = methodName+"."+sttm.getLineNumber();
			if(!this.statements.contains(stmName)){
				this.statements.add(stmName);
			}
		}
		
	}
	
	public int getCountUniqueClassCoverage(TestCase tc){
		int count = 0;
		List<String> classes = new ArrayList<String>();
		for (String clazz : tc.getClassCodeCoverage()) {
			if(!classes.contains(clazz)){
				classes.add(clazz);
				count++;
			}
		}
		return count;
	}
	
	public int getCountUniqueMethodCoverage(TestCase tc){
		int count = 0;
		List<String> methods = new ArrayList<String>();
		for (String method : tc.getMethodCoverage()) {
			if(!methods.contains(method)){
				methods.add(method);
				count++;
			}
		}
		return count;
	}
	
	public int getCountUniqueStatementCoverage(TestCase tc){
		int count = 0;
		List<String> sttms = new ArrayList<String>();
		for (String sttm : tc.getStatementsCoverage()) {
			if(!sttms.contains(sttm)){
				sttms.add(sttm);
				count++;
			}
		}
		return count;
	}
	
	public List<String> getTestCaseChangesCovered(TestCase tc) {
		List<String> sttms = new ArrayList<String>();
		for (String sttm : tc.getStatementsCoverage()) {
			if(!sttms.contains(sttm) && this.affectedBlocks.contains(sttm)) {
				sttms.add(sttm);
			}
		}
		return sttms;
	}

}
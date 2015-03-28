package com.edu.ufcg.splab.priorj.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.edu.ufcg.splab.core.Difference;
import com.edu.ufcg.splab.core.InstrumentClass;
import com.edu.ufcg.splab.coverage.*;
import com.edu.ufcg.splab.coverage.manager.Coverage;
import com.edu.ufcg.splab.priorj.report.GenerateCoverageReport;
import com.edu.ufcg.splab.priorj.report.GenerateExecutionOrderReport;
import com.edu.ufcg.splab.priorj.report.GenerateTestSuite;
import com.edu.ufcg.splab.priorj.report.GenerateTestSuiteForJUnit4;
import com.edu.ufcg.splab.priorj.technique.*;
import com.java.io.JavaIO;

import coverage.TestCase;
import coverage.TestSuite;



/**
 * Class PriorJ priorization of JUnit Test Cases. 
 *  
 * @author Samuel T. C. Santos
 *
 */
public class PriorJ {

	private static PriorJ instance;
	private static List<Integer> techniques;
	private static List<String> affectedBlocks;
	private static boolean junitFrameworkVersion4;
	private String projPath;

	public static PriorJ getInstance(){
		if (PriorJ.instance == null){
			techniques = new ArrayList<Integer>();
			affectedBlocks = new ArrayList<String>();
			PriorJ.instance = new PriorJ();
			junitFrameworkVersion4 = false;
		}
		return PriorJ.instance;
	}


	/**
	 * Adding the selected Technique by user.
	 * 
	 * @param typeOfTechnique
	 */
	public void addTechnique(int typeOfTechnique) {
		if (typeOfTechnique == TechniqueCreator.ADDITIONAL_METHOD_COVERAGE){
//			techniques.add(TechniqueCreator.ADDITIONAL_METHOD_COVERAGE);
		}
		else if(typeOfTechnique == TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE){
//			techniques.add(TechniqueCreator.ADDITIONAL_STATEMENT_COVERAGE);
		}
		else if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			techniques.add(TechniqueCreator.CHANGED_BLOCKS);
		}
		else if (typeOfTechnique == TechniqueCreator.RANDOM){
			techniques.add(TechniqueCreator.RANDOM);
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_METHOD_COVERAGE){
//			techniques.add(TechniqueCreator.TOTAL_METHOD_COVERAGE);
		}
		else if (typeOfTechnique == TechniqueCreator.TOTAL_STATEMENT_COVERAGE){
//			techniques.add(TechniqueCreator.TOTAL_STATEMENT_COVERAGE);
		}
		else if (typeOfTechnique == TechniqueCreator.ECHELON_CHANGED){
//			techniques.add(TechniqueCreator.ECHELON_CHANGED);
		}
		else{
			throw new IllegalArgumentException("Invalid Technique Type!");
		}
	}

	/**
	 * Removing a selected technique.
	 * 
	 * @param typeOfTechnique
	 */
	public void removeTechnique(int typeOfTechnique){
		if (techniques.contains(typeOfTechnique)){
			int index = techniques.indexOf(typeOfTechnique);
			techniques.remove(index);
		}
	}

	/**
	 * The list of selected Techniques Types.
	 * 
	 * @return
	 */
	public List<Integer> getTechniques() {
		return techniques;
	}

	/**
	 * Getting a list with all  Test Suites.
	 * 
	 * @param allSuites
	 * @return
	 */
	public List<TestSuite> getTestSuites(@SuppressWarnings("rawtypes") List<List> allSuites) {
		Coverage coverage = new Coverage();
		return coverage.getSuiteList(allSuites);
	}

	/**
	 * Getting a list with all Test Cases.
	 * 
	 * @param suites
	 * @return
	 */
	public List<TestCase> getTestCases(List<TestSuite> suites) {
		Coverage coverage = new Coverage();
		return coverage.getAllTests(suites);
	}


	/**
	 * Getting an selection (fraction) to generate the suite.
	 * 
	 * @param suiteSize
	 * @param allTests
	 * @return
	 */
	public List<String> getSelection(int suiteSize, List<String> allTests){
		int total = allTests.size();
		int selectedSize  = total * suiteSize/100; 
		return allTests.subList(0, selectedSize);
	}

	/**
	 * 
	 * @param totalMethodCoverage
	 * @param allTests
	 * @return
	 * @throws EmptySetOfTestCaseException 
	 */
	public List<String> prioritize(int typeOfTechnique, List<TestCase> allTests) throws EmptySetOfTestCaseException {
		TechniqueCreator creator = new TechniqueCreator();
		if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			TechniqueEchelonTotal technique = new TechniqueEchelonTotal();
			technique.setBlockAffected(affectedBlocks);
			return technique.prioritize(allTests);

		} else if (typeOfTechnique == TechniqueCreator.ECHELON_CHANGED){
			TechniqueEchelonChanged technique = new TechniqueEchelonChanged();
			technique.setAffectedBlocks(affectedBlocks);
			return technique.prioritize(allTests);
		} else {
			Technique technique = creator.create(typeOfTechnique);
			return technique.prioritize(allTests);
		}
		
	}

	public List<String> prioritizeExp(int typeOfTechnique, List<TestCase> allTests) throws EmptySetOfTestCaseException {
		TechniqueCreator creator = new TechniqueCreator();
		if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS){
			TechniqueEchelonTotal technique = new TechniqueEchelonTotal();
			technique.setBlockAffected(affectedBlocks);
			return technique.prioritize(allTests);

		} else if (typeOfTechnique == TechniqueCreator.ECHELON_CHANGED){
			TechniqueEchelonChanged technique = new TechniqueEchelonChanged();
			technique.setAffectedBlocks(affectedBlocks);
			return technique.prioritize(allTests);
		} else {
			Technique technique = creator.create(typeOfTechnique);
			return technique.prioritize(allTests);
		}
	}

	/**
	 * 
	 * 
	 * @param suiteSize
	 * 
	 * @param typeOfTechnique
	 * @param allTests
	 * @return
	 * @throws Exception 
	 */
	public List<String> prioritizeAll(int suiteSize, List<TestCase> allTests) throws Exception{
		List<String> prioritizedList = new ArrayList<String>();
		for (Integer typeOfTechnique : techniques){
			//getting the suite names
			String acronyms = TechniqueCreator.acronyms(typeOfTechnique);
			//prioritize the tests.
			prioritizedList = prioritize(typeOfTechnique, allTests);
			prioritizedList = getSelection(suiteSize, prioritizedList);
			creatPriorArtifact(prioritizedList, typeOfTechnique, acronyms);
		}
		return prioritizedList;
	}

	public List<String> prioritizeAllExp(int suiteSize, List<TestCase> allTests) throws Exception{
		List<String> prioritizedList = new ArrayList<String>();
		for (Integer typeOfTechnique : techniques){
			//getting the suite names
			String acronyms = TechniqueCreator.acronyms(typeOfTechnique);
			//prioritize the tests.
//			prioritizedList = prioritizeExp(typeOfTechnique, allTests);

			TechniqueCreator creator = new TechniqueCreator();
			List<String> weightList;
			List<String> notWeightList;
			if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS || 
					typeOfTechnique == TechniqueCreator.ECHELON_CHANGED){
				if (typeOfTechnique == TechniqueCreator.CHANGED_BLOCKS ){
					TechniqueEchelonTotal technique = new TechniqueEchelonTotal();
					technique.setBlockAffected(affectedBlocks);
					technique.weightList = new ArrayList<String>();
					technique.notWeightList = new ArrayList<String>();
					technique.prioritize(allTests);
					weightList = technique.weightList;
					notWeightList = technique.notWeightList;
				} else {
					TechniqueEchelonChanged technique = new TechniqueEchelonChanged();
					technique.setAffectedBlocks(affectedBlocks);
					technique.weightList = new ArrayList<String>();
					technique.notWeightList = new ArrayList<String>();
					technique.prioritize(allTests);
					weightList = technique.weightList;
					notWeightList = technique.notWeightList;
				}

				for (int i = 1; i < 41; i++) {
					prioritizedList = new ArrayList<String>();
					Collections.shuffle(notWeightList);
					prioritizedList = copy(weightList);
					prioritizedList.addAll(copy(notWeightList));
					creatPriorArtifact(prioritizedList, typeOfTechnique, acronyms+"_"+i);
				}
			}
			else{
				Technique technique = creator.create(typeOfTechnique);
				for (int i = 1; i < 41; i++) {
					prioritizedList = new ArrayList<String>();
					prioritizedList = technique.prioritize(allTests);
					creatPriorArtifact(prioritizedList, typeOfTechnique, acronyms+"_"+i);
				}

			}

			//			prioritizedList = getSelection(suiteSize, prioritizedList);
			//saving the produced artifacts

		}
		return prioritizedList;
	}


	private List<String> copy(List<String> weightList) {
		ArrayList<String> returnStr = new ArrayList<String>();
		for (String string : weightList) {
			returnStr.add(string);
		}
		return returnStr;
	}


	private void creatPriorArtifact(List<String> prioritizedList,
			Integer typeOfTechnique, String acronyms) throws Exception {
		String order = createOrderReport(typeOfTechnique, prioritizedList);
		DataManager.save(acronyms+".js","js", order);
		String suite = createSuite(acronyms, prioritizedList);
		DataManager.save(acronyms+".java", suite);
	}

	/**
	 * This method prioritize with many techniques simultaneously.
	 * @param projPath 
	 * @param size 
	 * 
	 * @throws Exception 
	 * 
	 */
	public void prioritizeAll(List<TestCase> allTests) throws Exception {
		for (Integer typeOfTechnique : techniques){
			//getting the suite names
			String acronyms = TechniqueCreator.acronyms(typeOfTechnique);
			//prioritize the tests.
			List<String> prioritizedList = prioritize(typeOfTechnique, allTests);
			creatPriorArtifact(prioritizedList, typeOfTechnique, acronyms);
		}
	}

	/**
	 * This method create a prioritized test suite from a list of tests.
	 * 
	 * @param suiteName
	 *  the suite name
	 * @param tests
	 *  the prioritized test order
	 * @return
	 *  the suite code
	 * @throws Exception 
	 */
	public String createSuite(String suiteName, List<String> tests) throws Exception{
		if (isJUnit4()){
			return GenerateTestSuiteForJUnit4.generate("tests", suiteName, tests);
		}
		else{
			return GenerateTestSuite.generate("tests", suiteName, tests);
		}
	}

	private boolean isJUnit4() {
		return junitFrameworkVersion4;
	}
	/**
	 * Set the JUnit Framework version as 4.x
	 * 
	 * @param option
	 * 		true if the Framework version is 4.x
	 * 		if the Framework version is 3.x set false;
	 */
	public void setJUnitFrameworkVersion4(boolean option){
		junitFrameworkVersion4 = true;
	}
	/**
	 * Create the order report.
	 * 
	 * @param listTests
	 *   a list of prioritized tests names.
	 *   
	 * @return
	 *  the order report.
	 *  
	 * @throws Exception 
	 */
	public String createOrderReport(int typeOfTechnique, List<String> listTests) throws Exception {
		return GenerateExecutionOrderReport.create(typeOfTechnique,listTests);
	}

	/**
	 * Create a coverage report.
	 *  
	 * @param suites
	 * @return
	 */
	public String createCoverageReport(List<TestSuite> suites) {
		GenerateCoverageReport textReport = new GenerateCoverageReport(suites);
		return textReport.generateCoverageReport();
	}

	/**
	 * This method instrument a class.
	 * 
	 * @param filePath
	 *  the path to the java file.
	 * @param file
	 *  the java file name.
	 * @param isTest
	 *  true or false indicating if the file is a test file.
	 *  
	 * @throws Exception 
	 */
	public void instrument(String filePath, String file, boolean isTest) throws Exception {
		InstrumentClass inst = new InstrumentClass(filePath, file, isTest);
		inst.instrumentationRun();
	}

	/**
	 * Check differences between two versions of same code.
	 * 
	 * @param pathCodeNew
	 * @param pathCodeOld
	 * @return
	 * @throws Exception
	 */
	public List<String> checkDifference(String filePath, String oldFilePath) throws Exception {
		Difference difference = new Difference(filePath, oldFilePath);
		difference.diff();
		List<String> affected = difference.getStatementsDiff();
		return affected;
	}


	/**
	 * 
	 * @param blocks
	 */
	public void setAffectedBlocks(List<String> blocks) {
		this.affectedBlocks = blocks;
	}	

	public static void main(String[] args) {
		System.out.println("PriorJ version 1.0.6");
	}

	public void setProjPath(String projPath) {
		this.projPath = projPath;
	}

}
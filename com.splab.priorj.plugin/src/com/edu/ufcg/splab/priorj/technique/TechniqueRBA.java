package com.edu.ufcg.splab.priorj.technique;


import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import main.FacadeRBA;

import coverage.TestCase;
import com.java.io.JavaIO;

import exceptions.DBException;


public class TechniqueRBA extends ModificationTechnique implements Technique {

	private List<String> affectedMehts;
	private String path;
	final String refFile = "refFile.txt";

	public TechniqueRBA(String path) {
		this.path = path;
	}

	public List<String> getBlockAffected() {
		return affectedMehts;
	}

	public void setAffectedMeths(List<String> affectedMehts) {
		this.affectedMehts = affectedMehts;
	}

	public double getWeight(List objectList) {
		int count = 0;
		Iterator<String> itObjects = objectList.iterator();
		while(itObjects.hasNext()){
			String obj = itObjects.next();
			obj = obj.replace("(", "([");
			obj = obj.replace(")", "])");
			if(containsMeth(obj)){
				count++;
			}
		}
		return count;
	}

	public boolean containsMeth(String value){
		return affectedMehts.contains(value);
	}

	@Override
	public List<String> prioritize(List<TestCase> tests)
			throws EmptySetOfTestCaseException {
		List<String> strs = treatStrs(runRBA());
		setAffectedMeths(strs);

		List<TestCase> copyList = new ArrayList<TestCase>(tests);

		List<String> suiteList = new ArrayList<String>();

		ArrayList<TestCaseComparable> weightList = new ArrayList<TestCaseComparable>();

		ArrayList<TestCase> notWeighted = new ArrayList<TestCase>();

		for (TestCase testCase : copyList) {
			double value = getWeight(testCase.getMethodCoverage());
			if (value != 0.0)
				weightList.add(new TestCaseComparable(value, testCase));
			else 
				notWeighted.add(testCase);
		}

		Collections.sort(weightList);
		List<String> weightListStr = new ArrayList<String>();
		List<String> notWeightListStr = new ArrayList<String>();

		for (int i=weightList.size()-1; i>=0; i--) {
			TestCaseComparable obj = weightList.get(i);
			String tcSig = obj.getTestCase().getSignature();
			suiteList.add(tcSig);

			weightListStr.add(tcSig);
		}

		this.weightList = weightListStr;

		Collections.shuffle(notWeighted);
		for (TestCase test: notWeighted){
			String tcSig = test.getSignature();
			suiteList.add(tcSig);

			notWeightListStr.add(tcSig);
		}
		this.notWeightList = notWeightListStr;


		return suiteList;
	}

	private List<String> treatStrs(List<String> rbaList) {
		List<String> returnList = new ArrayList<String>();
		for (String methStr : rbaList) {
			String returnStr = methStr.replace("*", ".");
			returnList.add(returnStr);
		}
		return returnList;
	}

	private List<String> runRBA() {
		FacadeRBA rba = new FacadeRBA();
		ArrayList<CompilationUnit> comps;
		try {
			comps = rba.parse(path);
			String[] params = getParameters();
			String type = params[0];
			switch (type) {
			case "AP":
				return rba.getImpactedElementsAddParameter(comps, params[1], params[2]);
			case "EM":
				return rba.getImpactedElementsExtractMethod(comps, params[1], params[2], params[3], Integer.parseInt(params[4]), Integer.parseInt(params[5]));
			case "MM":
				return rba.getImpactedElementsMoveMethod(comps, params[1], params[2], params[3]);
			case "PUF":
				return rba.getImpactedElementsPullUpField(comps, params[1], params[2], params[3]);
			case "PUM":
				return rba.getImpactedElementsPullUpMethod(comps, params[1], params[2], params[3]);
			case "RM":
				return rba.getImpactedElementsRenameMethod(comps, params[1], params[2], params[3]);
			default:
				break;
			}
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String[] getParameters() {
		Scanner input;
		String[] params;
		try {
			input = new Scanner(new File (path+JavaIO.SEPARATOR+refFile));
			while(input.hasNext()){
				String line = input.nextLine();
				if (line.equals("Add Parameter")){
					params = new String[3];
					params[0] = "AP";
					params[1] = input.nextLine();
					params[2] = input.nextLine();
					return params;
				}else{
					if (line.equals("Move Method")){
						params = new String[4];
						params[0] = "MM";
						params[1] = input.nextLine();
						params[2] = input.nextLine();
						params[3] = input.nextLine();
						return params;
					}else{
						if (line.equals("Extract Method")){
							params = new String[6];
							params[0] = "EM";
							params[1] = input.nextLine();
							params[2] = input.nextLine();
							params[3] = input.nextLine();
							params[4] = input.nextLine();
							params[5] = input.nextLine();
							return params;
						}else{
							if (line.equals("Pull Up Field")){
								params = new String[4];
								params[0] = "PUF";
								params[1] = input.nextLine();
								params[2] = input.nextLine();
								params[3] = input.nextLine();
								return params;
							}else{
								if (line.equals("Pull Up Method")){
									params = new String[4];
									params[0] = "PUM";
									params[1] = input.nextLine();
									params[2] = input.nextLine();
									params[3] = input.nextLine();
									return params;
								}else{
									if (line.equals("Rename Method")){
										params = new String[4];
										params[0] = "RM";
										params[1] = input.nextLine();
										params[2] = input.nextLine();
										params[3] = input.nextLine();
										return params;
									}
								}
							}
						}
					}
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}

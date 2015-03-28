package com.edu.ufcg.splab.priorj.technique;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coverage.TestCase;


public class TechniqueEchelonChanged extends ModificationTechnique implements Technique {
	
	private List<String> blockAffected;
    
	public TechniqueEchelonChanged(){
	}
	
    public List<String> getBlockAffected() {
		return blockAffected;
	}

	public void setBlockAffected(List<String> blockAffected) {
		this.blockAffected = blockAffected;
	}
	
	public boolean containsBlock(String value){
		return blockAffected.contains(value);
	}
	
	public double getPercentage(double value){
		int sizeBlock = getBlockAffected().size();
		if(sizeBlock == 0) return 0;
		return value/sizeBlock;
	}

    public List<String> getWeight(final List objectList) {
        Iterator<String> itObjects = objectList.iterator();
        List<String> statementCoverage = new ArrayList<String>();
        while(itObjects.hasNext()){
        	String obj = itObjects.next();
        	if(containsBlock(obj)){
        		statementCoverage.add(obj);
        	}
        }
        return statementCoverage;
    }
    
    /**
     * Calculate the test case weight.
     * 
     * @param weightedList
     * 				The weight list to be populated.
     * @param notWeightedList
     * 				The not weight list to be populated.
     * @param copyList
     * 				The testcase list to be analized.
     */
    private void calulateWeight(List<TestCaseEchelonComparable> weightedList, List<TestCase> notWeightedList,
    		final List<TestCase> copyList) {
    	for (TestCase testCase : copyList) {
        	List<String> statementCoverage = getWeight(testCase.getStatementsCoverage());
        	if (statementCoverage.size() != 0.0)
        		weightedList.add(new TestCaseEchelonComparable(getPercentage(statementCoverage.size()),
        				testCase, statementCoverage));
        	else 
        		notWeightedList.add(testCase);
		}
    }
    
	@Override
	public List<String> prioritize(List<TestCase> tests)throws EmptySetOfTestCaseException {
		List<TestCase> copyList = new ArrayList<TestCase>(tests);
        List<String> suiteList = new ArrayList<String>();
        ArrayList<TestCaseEchelonComparable> weightedList = new ArrayList<TestCaseEchelonComparable>();
        ArrayList<TestCase> notWeightedList = new ArrayList<TestCase>();
        
        calulateWeight(weightedList, notWeightedList, copyList);
        
        System.out.println("Weighted:");
        for (TestCaseEchelonComparable testCase : weightedList) {
			System.out.println(testCase.getTestCase().getName() + " " + testCase.getWeight());
		}
        System.out.println("Not Weighted:");
        for (TestCase testCase : notWeightedList) {
        	System.out.println(testCase.getName());
		}
    	return null;
    }
}

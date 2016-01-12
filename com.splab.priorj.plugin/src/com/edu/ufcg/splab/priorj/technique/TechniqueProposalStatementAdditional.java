package com.edu.ufcg.splab.priorj.technique;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2015  Berg E. S. Cavalcante
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.edu.ufcg.splab.priorj.coverage.model.TestCase;

/**
 * 
 * This class provides a Echelon changed algorithm based on affectedblocks coverage.
 * 
 * @author Berg E. S. Cavalcante.
 * @version 1.1
 *
 */
public class TechniqueProposalStatementAdditional extends ModificationTechnique implements Technique {
	
	private List<String> affectedBlocks;
	
	private Map<String, List<String>> deletedCoverages;
	
    public List<String> getAffectedBlocks() {
		return affectedBlocks;
	}

	public void setAffectedBlocks(List<String> affectedBlocks) {
		this.affectedBlocks = affectedBlocks;
	}
	
	public void setDeletedCoverages(Map<String, List<String>> deletedCoverages) {
		this.deletedCoverages = deletedCoverages;
	}
	
	public boolean containsStatement(final String statement) {
		return this.affectedBlocks.contains(statement);
	}
	
	/**
	 * Get the percentage based in affected blocks.
	 * 
	 * @param weight
	 * 			Weight of changed coverage statements.
	 * @return
	 */
	private double getPercentage(final double weight){
		int sizeBlock = getAffectedBlocks().size();
		if(sizeBlock == 0) {
			return 0;
		}
		return weight/sizeBlock;
	}

	/**
	 * Get the weight.
	 * 
	 * @param objectList
	 * 				List with all test case coverage statements.
	 * @return double with the test .
	 */
	public double getWeight(final TestCase test) {
		int count = 0;
		Iterator<String> itObjects = this.getCoverageDistinctWithDeletedStatements(test).iterator();
		List<String> clone = new ArrayList<String>(this.affectedBlocks);
		while(itObjects.hasNext()){
			String obj = itObjects.next();
			if(containsStatement(obj)) {
				count++;
				clone.remove(obj);
			}
		}
		return getPercentage(count);
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
    private void calulateWeight(List<TestCaseProposal> weightedList, List<TestCase> notWeightedList,
    		final List<TestCase> copyList) {
    	TestCase test = null;
    	while(!copyList.isEmpty() && !this.affectedBlocks.isEmpty()){
            test = biggerWeight(copyList, weightedList, notWeightedList);
            if (!test.getSignature().isEmpty() && getWeight(test) > 0.0) {
                copyList.remove(test);
                weightedList.add(new TestCaseProposal(getWeight(test),
                		test, this.getCoverageDistinctWithDeletedStatements(test)));
            } else {
            	copyList.remove(test);
            	notWeightedList.add(test);
            }
        }
    }
    
    /**
     * Calculate the score.
     * 
     * @param weightedList
     * 				The weighted list.
     */
    private void calculateScore(final List<TestCaseProposal> weightedList) {
    	List<String> controlList = new ArrayList<String>();
    	// Incremento estático.
//    	double incFactor = 1.0 / (double) this.affectedBlocks.size();
//    	double decFactor = 1.0 / (2.0 * this.affectedBlocks.size());
    	
    	for (TestCaseProposal testCaseEchelon : weightedList) {
    		double score = testCaseEchelon.getWeight();
//    		// Incremento dinâmico.
    		double incFactor = 1.0 / (double) this.getCoverageDistinctWithDeletedStatements(testCaseEchelon.getTestCase()).size();
        	double decFactor = 1.0 / (2.0 * this.getCoverageDistinctWithDeletedStatements(testCaseEchelon.getTestCase()).size());
    		// Primeiro elemento é quem dita a lista de controle.
    		if(weightedList.get(0).equals(testCaseEchelon)) {
    			score += testCaseEchelon.getWeight()*incFactor;
    			controlList = testCaseEchelon.getStatementCoverage();
    			testCaseEchelon.setScore(score);
    			continue;
    		}
    		
    		for (String change : testCaseEchelon.getStatementCoverage()) {
    			if(controlList.contains(change)) {
    				score -= decFactor;
    			} else {
    				score += incFactor;
    				controlList.add(change);
    			}
    		}
    		testCaseEchelon.setScore(score);
		}
    }
    
    /**
     * Get the test cases signature of weighted list.
     * 
     * @param weightedList
     * 				List<TestCase> weightedList.
     * 
     * @return List<String> with the signatureList.
     */
    private List<String> getSignatureWeightedList(final List<TestCaseProposal> weightedList) {
    	List<String> suiteList = new ArrayList<String>();
    	for (TestCaseProposal testCaseEchelon : weightedList) {
    		String tcSig = testCaseEchelon.getTestCase().getSignature();
			suiteList.add(tcSig);
		}
    	return suiteList;
    }
    
    /**
     * Get the test cases signature of not weighted list.
     * 
     * @param notWeightedList
     * 				List<TestCase> notWeightedList.
     * 
     * @return List<String> with the signatureList.
     */
    private List<String> getSignatureNotWeightedList(final List<TestCase> notWeightedList) {
    	List<String> suiteList = new ArrayList<String>();
    	for (TestCase testCase : notWeightedList) {
    		String tcSig = testCase.getSignature();
			suiteList.add(tcSig);
		}
    	return suiteList;
    }
    
    public TestCase biggerWeight(List<TestCase> copyList, List<TestCaseProposal> weightedList, List<TestCase> notWeightedList){
        int indexBigger = 0;
        double bigger=0;
        
        int index=0;
        for (TestCase test: copyList){
        	double weight = getWeight(test);
            if (weight > bigger) {
                indexBigger = index;
                bigger = weight;
            }
            index++;
        }
        
        List<TestCase> sameWeight = new ArrayList<TestCase>();
        
        for (TestCase test: copyList) {
        	// Insere as linhas previamente deletadas (caso a mudança seja uma deleção).
        	List<String> distinct = this.getCoverageDistinctWithDeletedStatements(test);
        	double weight = getWeight(test);
            if (weight == bigger){
                sameWeight.add(test);
            }
        }
        
        if (sameWeight.size()>1)
            Collections.shuffle(sameWeight);
        
        return  sameWeight.size() > 0 ? sameWeight.get(0): new TestCase("");
        
    }
    
    private List<String> getCoverageDistinctWithDeletedStatements(final TestCase test) {
    	// Insere as linhas previamente deletadas (caso a mudança seja uma deleção).
    	List<String> deleted = deletedCoverages.get(test.getSignature());
    	List<String> distinct = test.getStatementsCoverageDistinct();
    	if(distinct != null && deleted != null) {
    		distinct.addAll(deleted);
    	}
    	return distinct;
    }
    
	@Override
	public List<String> prioritize(List<TestCase> tests)throws EmptySetOfTestCaseException {
		List<TestCase> copyList = new ArrayList<TestCase>(tests);
        ArrayList<TestCaseProposal> weightedList = new ArrayList<TestCaseProposal>();
        ArrayList<TestCase> notWeightedList = new ArrayList<TestCase>();
        System.out.println("Technique Proposal Statement Additional");
        calulateWeight(weightedList, notWeightedList, copyList);
        Collections.sort(weightedList, new EchelonComparator(EchelonComparator.BY_WEIGHT));
        Collections.reverse(weightedList);
        calculateScore(weightedList);
        Collections.sort(weightedList, new EchelonComparator(EchelonComparator.BY_SCORE));
        Collections.reverse(weightedList);
        
        System.out.println("By Score:");
        int i = 0;
        for (TestCaseProposal testCaseEchelon : weightedList) {
			System.out.println("[" + i +"] " + testCaseEchelon.getTestCase() 
					+ " [S - " +testCaseEchelon.getScore() 
					+ " | W - " + testCaseEchelon.getWeight() + "]");
			i++;
        }
        
        List<String> suiteList = getSignatureWeightedList(weightedList);
    	this.weightList = new ArrayList<String> (suiteList);
        this.notWeightList = new ArrayList<String>(getSignatureNotWeightedList(notWeightedList));
    	suiteList.addAll(this.notWeightList);
        
    	return suiteList;
    }
}

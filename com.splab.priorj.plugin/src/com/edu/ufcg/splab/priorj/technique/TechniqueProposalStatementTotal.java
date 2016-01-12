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
public class TechniqueProposalStatementTotal extends ModificationTechnique implements Technique {
	
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
	
	public boolean containsBlock(final String block){
		return affectedBlocks.contains(block);
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
		if(sizeBlock == 0) return 0;
		return weight/sizeBlock;
	}

	/**
	 * Get the weight.
	 * 
	 * @param objectList
	 * 				List with all test case coverage statements.
	 * @return A List<String> with the test case coverage changed statements.
	 */
    private List<String> getWeight(final List objectList) {
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
    private void calulateWeight(List<TestCaseProposal> weightedList, List<TestCase> notWeightedList,
    		final List<TestCase> copyList) {
    	for (TestCase testCase : copyList) {
        	List<String> weight = testCase.getStatementsCoverage();
        	List<String> deleted = deletedCoverages.get(testCase.getSignature());
        	if(deleted != null) {
        		weight.addAll(deleted);
        	}
    		List<String> statementCoverage = getWeight(weight);
        	if (statementCoverage.size() != 0.0) {
        		weightedList.add(new TestCaseProposal(getPercentage(statementCoverage.size()),
        				testCase, statementCoverage));
        	}
        	else {
        		notWeightedList.add(testCase);
        	}
		}
    }
    
    /**
     * Create the statement echelon list.
     * 
     * @param weightedList
     * 				The weighted list.
     * 
     * @return List<StatementEchelonChanged>.
     */
    private List<StatementProposal> createStatementEchelonList(
    		final List<TestCaseProposal> weightedList) {
    	List<StatementProposal> statements = new ArrayList<StatementProposal>();
    	
    	// Itera sobre todos os blocos afetados.
    	for (String block : affectedBlocks) {
    		List<TestCase> testCases = new ArrayList<TestCase>();
			/*
			 *  Itera sobre todos os casos de teste a fim de buscar os casos de teste
			 *  que cobrem esse bloco afetado.
			 */
    		for (TestCaseProposal testCase : weightedList) {
				if (testCase.getStatementCoverage().contains(block)) {
					testCases.add(testCase.getTestCase());
				}
			}
    		StatementProposal st = new StatementProposal(block, testCases);
    		statements.add(st);
    		System.out.println(st);
    	}
    	
    	return statements;
    }
    
    /**
     * Calculate the score.
     * 
     * @param weightedList
     * 				The weighted list.
     */
    private void calculateScore(final List<TestCaseProposal> weightedList) {
//    	List<StatementEchelonChanged> statementsChanged = createStatementEchelonList(weightedList);
    	List<String> controlList = new ArrayList<String>();
    	// Incremento estático.
//    	double incFactor = 1.0 / (double) this.affectedBlocks.size();
//    	double decFactor = 1.0 / (2.0 * this.affectedBlocks.size());
    	
    	for (TestCaseProposal testCaseEchelon : weightedList) {
    		double score = testCaseEchelon.getWeight();
    		// Incremento dinâmico.
    		double incFactor = 1.0 / (double) testCaseEchelon.getStatementCoverage().size();
        	double decFactor = 1.0 / (2.0 * testCaseEchelon.getStatementCoverage().size());
    		
    		// Primeiro elemento é quem dita a lista de controle.
    		if(weightedList.get(0).equals(testCaseEchelon)) {
    			score += testCaseEchelon.getStatementCoverage().size()*incFactor;
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
    
	@Override
	public List<String> prioritize(List<TestCase> tests)throws EmptySetOfTestCaseException {
		List<TestCase> copyList = new ArrayList<TestCase>(tests);
        ArrayList<TestCaseProposal> weightedList = new ArrayList<TestCaseProposal>();
        ArrayList<TestCaseProposal> scoredList = new ArrayList<TestCaseProposal>();
        ArrayList<TestCase> notWeightedList = new ArrayList<TestCase>();
        System.out.println("Technique Proposal Statement Total");
        calulateWeight(weightedList, notWeightedList, copyList);
        
        Collections.sort(weightedList, new EchelonComparator(EchelonComparator.BY_WEIGHT));
        Collections.reverse(weightedList);
        for (TestCaseProposal testCase : weightedList) {
			scoredList.add(testCase);
		}
        
        calculateScore(weightedList);
        
        Collections.sort(weightedList, new EchelonComparator(EchelonComparator.BY_SCORE));
        Collections.reverse(weightedList);
        System.out.println("By Score:");
        int i = 0;
        for (TestCaseProposal testCaseEchelon : weightedList) {
			System.out.println("[" + i +"] " + testCaseEchelon.getTestCase() 
					+ " [S - " +testCaseEchelon.getScore() 
					+ " | W - " + testCaseEchelon.getWeight() + "]"
					+ " ------ " + scoredList.get(i).getTestCase() 
					+ " [W - " + scoredList.get(i).getWeight() + "]");
			i++;
        }
        
        List<String> suiteList = getSignatureWeightedList(weightedList);
    	this.weightList = new ArrayList<String> (suiteList);
        this.notWeightList = new ArrayList<String>(getSignatureNotWeightedList(notWeightedList));
    	suiteList.addAll(this.notWeightList);
        return suiteList;
    }
}

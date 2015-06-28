package com.edu.ufcg.splab.priorj.technique;

import java.util.List;

import com.edu.ufcg.splab.priorj.coverage.model.TestCase;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Berg E. S. Cavalcante
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

/**
 * This class is used to sorting a set of test case by weight values. 
 * 
 * @author Berg Élisson.
 * @version 1.1
 */
public class TestCaseEchelon {

	private double weight;
	private TestCase testCase;
	private List<String> statementCoverage;
	private double score;
	
	/**
	 * The constructor to TestCaseComparable.
	 * 
	 * @param weight a weight for this test case.
	 * @param testCase a test case.
	 */
	public TestCaseEchelon(final double weight, final TestCase testCase,
			final List<String> statementCoverage){
		this.weight = weight;
		this.testCase = testCase;
		this.statementCoverage = statementCoverage;
	}
	
	/**
	 * Get the value of weight for this test case.
	 * 
	 * @return the weight.
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Get the statement coverage list.
	 * 
	 * @return the statement coverage list.
	 */
	public List<String> getStatementCoverage() {
		return statementCoverage;
	}

	/**
	 * Get the score.
	 * 
	 * @return The Score.
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * Set the score.
	 */
	public void setScore(final double score) {
		this.score = score;
	}

	/**
	 * Return the test case.
	 * 
	 * @return a object type <code>TestCase</code>
	 */
	public TestCase getTestCase() {
		return testCase;
	}

	@Override
	public String toString() {
		return "TestCaseEchelonComparable [weight=" + weight + ", testCase="
				+ testCase + ", statementCoverage=" + statementCoverage
				+ ", score=" + score + "]";
	}
}

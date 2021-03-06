package com.edu.ufcg.splab.priorj.technique;

import java.util.List;

import com.edu.ufcg.splab.priorj.coverage.model.TestCase;

public class StatementProposal {

	private String statement;
	private List<TestCase> testCases;
	
	public StatementProposal(String statement, List<TestCase> testcases) {
		super();
		this.statement = statement;
		this.testCases = testcases;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<TestCase> getTestcases() {
		return testCases;
	}

	public void setTestcases(List<TestCase> testcases) {
		this.testCases = testcases;
	}

	public boolean containsTestCase(final TestCase testcase) {
		return this.testCases.contains(testcase);
	}
	
	@Override
	public String toString() {
		return "StatementEchelonChanged [statement=" + statement
				+ ", testcases=" + testCases + "]";
	}
	
}

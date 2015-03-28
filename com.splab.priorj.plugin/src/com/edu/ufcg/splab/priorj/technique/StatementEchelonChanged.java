package com.edu.ufcg.splab.priorj.technique;

import java.util.List;

import coverage.TestCase;

public class StatementEchelonChanged {

	private String statement;
	private List<TestCase> testcases;
	
	public StatementEchelonChanged(String statement, List<TestCase> testcases) {
		super();
		this.statement = statement;
		this.testcases = testcases;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public List<TestCase> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<TestCase> testcases) {
		this.testcases = testcases;
	}

	@Override
	public String toString() {
		return "StatementEchelonChanged [statement=" + statement
				+ ", testcases=" + testcases + "]";
	}
	
}

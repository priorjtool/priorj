package com.edu.ufcg.splab.tests.priorj.coverage.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.edu.ufcg.splab.coverage.dao.Repository;
import com.edu.ufcg.splab.priorj.coverage.controller.TestSuiteController;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;

public class TestRepository {

	@Test
	public void testCreateRepositoryObject() {
		Repository r = new Repository();
		Assert.assertNotNull(r);
	}
	
	@Test
	public void test() {
		TestSuiteController t = new TestSuiteController();
		t.initDatabase();
		List<TestSuite> ts = t.getAll();
		for (TestSuite t1 : ts) {
			System.out.println(t1.teste());
		}
	}

}

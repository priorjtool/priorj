package com.edu.ufcg.splab.tests.priorj.coverage.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.edu.ufcg.splab.priorj.coverage.controller.TestSuiteController;
import com.edu.ufcg.splab.priorj.coverage.model.ClassCode;
import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;
import com.edu.ufcg.splab.priorj.coverage.model.TestCase;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;

public class TestSuiteControllerTest {

	private TestSuiteController controller;
	@Before
	public void setUp() {
		 this.controller = new TestSuiteController();
//		 this.controller.clearDb();
	}
	
	private TestSuite createTestSuite() {
		// ClassCode.
		ClassCode cc = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests", "TestSuiteControllerTest");
		// Statements.
		Statement st1 = new Statement("25");
		Statement st2 = new Statement("26");
		// Methods.
		Method m = new Method("setUp()");
		m.addStatement(st1);
		m.addStatement(st2);
		cc.addMethod(m);
		// TestCase.
		TestCase tc = new TestCase("testAdd()");
		tc.addClassCoverage(cc);
		tc.setNumberMethodsCoveredDistinct(1);
		tc.setNumberStatementsCoverageDistinct(2);
		tc.setSignature("public void testAdd()");
		// TestSuite.
		TestSuite ts = new TestSuite("com.edu.ufcg.splab.priorj.coverage.model.tests", "TestSuiteControllerTest");
		ts.addTestCase(tc);
		ts.setName("AllTests");
		return ts;
	}
	
//	@Test
//	public void test() {
//		List<TestSuite> suite = new ArrayList<TestSuite>();
//		TestSuite ts = createTestSuite();
//		System.out.println("Salvando");
//		
//		ts.teste();
//		for (TestCase t : ts.getTestCases()) {
//			System.out.println(t.teste());
//		}
//		suite.add(ts);
//		try {
//			this.controller.save(suite);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		System.out.println("From Database");
//		suite = this.controller.getAll();
//		for (TestSuite testSuite : suite) {
//			testSuite.teste();
//			for (TestCase t : testSuite.getTestCases()) {
//				System.out.println(t.teste());
//			}
//		}
//		
//	}
	
//	@Test
//	public void testAdd() {
//		TestSuite ts = createTestSuite();
//		this.controller.add(ts);
//		System.out.println(ts);
//		TestSuite m = this.controller.get(ts);
//		System.out.println(m);
//		assertEquals(ts, m);
//	}
//	
//	@Test
//	public void testRemove() {
//		Method method = new Method("testAdd");
//		this.controller.add(method);
//		Method m = this.controller.get(method);
//		assertEquals(method, m);
//		this.controller.remove(method);
//		m = this.controller.get(method);
//		assertEquals(null, m);
//	}
//	
//	@Test
//	public void testDuplicatedClassCode() {
//		Method method = new Method("testAdd");
//		this.controller.add(method);
//		this.controller.add(method);
//		assertEquals(1L, this.controller.getQuantityObjects());
//	}
//	
//	@Test
//	public void testRemoveAnAbsentObject() {
//		Method method = new Method("testAdd");
//		this.controller.remove(method);
//		Method m = this.controller.get(method);
//		assertEquals(null, m);
//	}
//	
//	@Test
//	public void testGetAnAbsentObject() {
//		Method method = new Method("testAdd");
//		Method m = this.controller.get(method);
//		assertEquals(null, m);
//	}
	
//	public void persistStatements(final Method method) {
//    	this.stControl.getEntityManager().getTransaction().begin();
//    	for (Statement st : method.getStatementCoverage()) {
//			Statement temp = this.stControl.get(st);
//			if(temp == null) {
//				this.stControl.add(st);
//			}
//		}
//    	this.stControl.getEntityManager().getTransaction().commit();
//    }
//	
//	@Test
//	public void testSavingAStatementBySavingAMethod() {
//		List<Statement> sts = new ArrayList<Statement>();
//		sts.add(new Statement("24"));
//		sts.add(new Statement("25"));
//		sts.add(new Statement("26"));
//		sts.add(new Statement("27"));
//		Method method = new Method("testAdd");
//		method.setStatementCoverage(sts);
//		this.persistStatements(method);
//		this.controller.add(method);
//		Method m = this.controller.get(method);
//		for (Statement statement : m.getStatementCoverage()) {
//			System.out.println(statement.teste());
//		}
//	}

}

package com.edu.ufcg.splab.tests.priorj.coverage.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.edu.ufcg.splab.priorj.coverage.controller.MethodController;
import com.edu.ufcg.splab.priorj.coverage.controller.StatementController;
import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;

public class MethodControllerTest {

	private MethodController controller;
	private StatementController control;
	@Before
	public void setUp() {
		 this.controller = new MethodController();
		 this.control = new StatementController();
		 this.controller.clearDb();
	}
	
	@Test
	public void testAdd() {
		Method method = new Method("testAdd");
		List<Statement> sts = new ArrayList<Statement>();
		Statement st = new Statement("24");
		Statement st1 = new Statement("24");
		
		sts.add(st);
		sts.add(st1);
		this.control.add(st);
		this.control.add(st1);
//		sts.add(new Statement("25"));
//		sts.add(new Statement("26"));
//		sts.add(new Statement("27"));
		
		method.setStatementCoverage(sts);
		this.controller.add(method);
		System.out.println(method.teste());
		Method m = this.controller.get(method);
		
		System.out.println(m.teste());
		assertEquals(method, m);
	}
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
package com.edu.ufcg.splab.tests.priorj.coverage.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.edu.ufcg.splab.priorj.coverage.controller.ClassCodeController;
import com.edu.ufcg.splab.priorj.coverage.model.ClassCode;

public class ClassCodeControllerTest {

	private ClassCodeController controller;
	
	@Before
	public void setUp() {
		 this.controller = new ClassCodeController();
		 this.controller.clearDb();
	}
	
	@Test
	public void testAdd() {
		ClassCode classCode = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests",
				"ClassCodeControllerTest");
		this.controller.add(classCode);
		ClassCode cc = this.controller.get(classCode);
		assertEquals(classCode, cc);
	}
	
	@Test
	public void testRemove() {
		ClassCode classCode = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests",
				"ClassCodeControllerTest");
		this.controller.add(classCode);
		this.controller.remove(classCode);
		ClassCode cc = this.controller.get(classCode);
		assertEquals(null, cc);
	}
	
	@Test
	public void testDuplicatedClassCode() {
		ClassCode classCode = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests",
				"ClassCodeControllerTest");
		this.controller.add(classCode);
		this.controller.add(classCode);
		assertEquals(1L, this.controller.getQuantityObjects());
	}
	
	@Test
	public void testRemoveAnAbsentObject() {
		ClassCode classCode = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests",
				"ClassCodeControllerTest");
		this.controller.remove(classCode);
		ClassCode cc = this.controller.get(classCode);
		assertEquals(null, cc);
	}
	
	@Test
	public void testGetAnAbsentObject() {
		ClassCode classCode = new ClassCode("com.edu.ufcg.splab.priorj.coverage.model.tests",
				"ClassCodeControllerTest");
		ClassCode cc = this.controller.get(classCode);
		assertEquals(null, cc);
	}

}

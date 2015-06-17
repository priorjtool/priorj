package com.edu.ufcg.splab.priorj.coverage.controller;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.edu.ufcg.splab.priorj.controller.PriorJ;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;

/**
 * <code>TestSuiteController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class TestSuiteController extends Controller {

	private static TestSuiteController instance;
	
    public static TestSuiteController getInstance() {
    	instance = new TestSuiteController();
    	return instance;
    }
    
    /**
     * Add a testSuite object into the database.
     * 
     * @param testSuite object.
     */
    public void add(final TestSuite testSuite) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().persist(testSuite);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Add List<TestSuite> into the database.
     * 
     * @param suites
     * 			List<TestSuite> suites.
     */
    public void add(final List<TestSuite> suites) {
    	for (TestSuite testSuite : suites) {
			add(testSuite);
		}
    }
    
    /**
     * Remove an existing TestSuite Objects.
     * 
     * @param testSuite 
     * 			TestSuite object. If the object does not exist nothing happens.
     */
    public void remove(final TestSuite testSuite) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().remove(testSuite);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Get an existing TestSuite object.
     * 	
     * @param testSuite 
     * 			TestSuite object.
     * 
     * @return The TestSuite object. If the object does not exist return null.
     */
    public TestSuite get(final TestSuite testSuite) {
    	this.getEntityManager().getTransaction().begin();
    	TestSuite tc = this.getEntityManager().find(TestSuite.class, testSuite);
    	this.getEntityManager().getTransaction().commit();
    	return tc;
    	
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<TestSuite> with all objects presents in database.
     */
    public List<TestSuite> getAll() {
    	TypedQuery<TestSuite> query =
    		    this.getEntityManager().createQuery("SELECT p FROM TestSuite p", TestSuite.class);
    	List<TestSuite> results = query.getResultList();
    	return results;
    }
    
    /**
     * Get the quantity of all objects presents in database.
     * 
     * @return long with the quantity.
     */
    public long getQuantityObjects() {
    	Query q1 = this.getEntityManager().createQuery("SELECT COUNT(p) FROM TestSuite p");
    	return (long) q1.getSingleResult();
    }
    
    /**
     * Clear database.
     */
    public void clearDb() {
    	TypedQuery<TestSuite> query =
    		    this.getEntityManager().createQuery("SELECT p FROM TestSuite p", TestSuite.class);
    	List<TestSuite> results = query.getResultList();
    	this.getEntityManager().getTransaction().begin();
    	for (TestSuite ts : results) {
			this.getEntityManager().remove(ts);
		}
    	this.getEntityManager().getTransaction().commit();
    }
    
}

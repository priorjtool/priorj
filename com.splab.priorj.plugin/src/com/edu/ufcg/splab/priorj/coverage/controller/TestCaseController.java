package com.edu.ufcg.splab.priorj.coverage.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;
import com.edu.ufcg.splab.priorj.coverage.model.TestCase;

/**
 * <code>TestCaseController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class TestCaseController extends Controller {

    /**
     * <code>TestCaseController</code> Constructor.
     */
    public TestCaseController() {
		super();
	}
    
    /**
     * Add a testCase object into the database.
     * 
     * @param testCase object.
     */
    public void add(final TestCase testCase) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().persist(testCase);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Remove an existing testCase Objects.
     * 
     * @param testCase 
     * 			TestCase object. If the object does not exist nothing happens.
     */
    public void remove(final TestCase testCase) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().remove(testCase);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Get an existing testCase object.
     * 	
     * @param testCase 
     * 			TestCase object.
     * 
     * @return The statement object. If the object does not exist return null.
     */
    public TestCase get(final TestCase testCase) {
    	this.getEntityManager().getTransaction().begin();
    	TestCase tc = this.getEntityManager().find(TestCase.class, testCase);
    	this.getEntityManager().getTransaction().commit();
    	return tc;
    	
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<TestCase> with all objects presents in database.
     */
    public List<TestCase> getAll() {
    	TypedQuery<TestCase> query =
    		    this.getEntityManager().createQuery("SELECT p FROM TestCase p", TestCase.class);
    	List<TestCase> results = query.getResultList();
    	return results;
    }
    
    /**
     * Get the quantity of all objects presents in database.
     * 
     * @return long with the quantity.
     */
    public long getQuantityObjects() {
    	Query q1 = this.getEntityManager().createQuery("SELECT COUNT(p) FROM TestCase p");
    	return (long) q1.getSingleResult();
    }
    
    /**
     * Clear database.
     */
    public void clearDb() {
    	TypedQuery<TestCase> query =
    		    this.getEntityManager().createQuery("SELECT p FROM TestCase p", TestCase.class);
    	List<TestCase> results = query.getResultList();
    	this.getEntityManager().getTransaction().begin();
    	for (TestCase tc : results) {
			this.getEntityManager().remove(tc);
		}
    	this.getEntityManager().getTransaction().commit();
    }
}

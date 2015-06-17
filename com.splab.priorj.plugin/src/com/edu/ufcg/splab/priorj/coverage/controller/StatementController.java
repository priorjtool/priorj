package com.edu.ufcg.splab.priorj.coverage.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;

/**
 * <code>StatementController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class StatementController extends Controller {

    /**
     * <code>StatementController</code> Constructor.
     */
    public StatementController() {
		super();
	}
    
    /**
     * Add a statement object into the database.
     * 
     * @param statement object.
     */
    public void add(final Statement statement) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().persist(statement);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Remove an existing statement Objects.
     * 
     * @param statement 
     * 			Statement object. If the object does not exist nothing happens.
     */
    public void remove(final Statement statement) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().remove(statement);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Get an existing statement object.
     * 	
     * @param statement 
     * 			Statement object.
     * 
     * @return The statement object. If the object does not exist return null.
     */
    public Statement get(final Statement statement) {
    	this.getEntityManager().getTransaction().begin();
    	Statement s = this.getEntityManager().find(Statement.class, statement);
    	this.getEntityManager().getTransaction().commit();
    	return s;
    	
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<Statement> with all objects presents in database.
     */
    public List<Statement> getAll() {
    	TypedQuery<Statement> query =
    		    this.getEntityManager().createQuery("SELECT p FROM Statement p", Statement.class);
    	List<Statement> results = query.getResultList();
    	return results;
    }
    
    /**
     * Get the quantity of all objects presents in database.
     * 
     * @return long with the quantity.
     */
    public long getQuantityObjects() {
    	Query q1 = this.getEntityManager().createQuery("SELECT COUNT(p) FROM Statement p");
    	return (long) q1.getSingleResult();
    }
    
    /**
     * Clear database.
     */
    public void clearDb() {
    	TypedQuery<Statement> query =
    		    this.getEntityManager().createQuery("SELECT p FROM Statement p", Statement.class);
    	List<Statement> results = query.getResultList();
    	this.getEntityManager().getTransaction().begin();
    	for (Statement st : results) {
			this.getEntityManager().remove(st);
		}
    	this.getEntityManager().getTransaction().commit();
    }
}

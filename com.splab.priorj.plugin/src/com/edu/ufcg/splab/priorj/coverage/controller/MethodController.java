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
 * <code>MethodController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class MethodController extends Controller {
    
    /**
     * <code>MethodController</code> Contructor.
     */
    public MethodController() {
    	super();
    }
    
    /**
     * Add a method object into the database.
     * 
     * @param method object.
     */
    public void add(final Method method) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().persist(method);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Remove an existing method Objects.
     * 
     * @param method 
     * 			Method object. If the object does not exist nothing happens.
     */
    public void remove(final Method method) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().remove(method);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Get an existing method object.
     * 	
     * @param method 
     * 			Method object.
     * 
     * @return The method object. If the object does not exist return null.
     */
    public Method get(final Method method) {
    	this.getEntityManager().getTransaction().begin();
    	Method m = this.getEntityManager().find(Method.class, method);
    	this.getEntityManager().getTransaction().commit();
    	return m;
    	
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<Method> with all objects presents in database.
     */
    public List<Method> getAll() {
    	TypedQuery<Method> query =
    		    this.getEntityManager().createQuery("SELECT p FROM Method p", Method.class);
    	List<Method> results = query.getResultList();
    	return results;
    }
    
    /**
     * Get the quantity of all objects presents in database.
     * 
     * @return long with the quantity.
     */
    public long getQuantityObjects() {
    	Query q1 = this.getEntityManager().createQuery("SELECT COUNT(p) FROM Method p");
    	return (long) q1.getSingleResult();
    }
    
    /**
     * Clear database.
     */
    public void clearDb() {
    	TypedQuery<Method> query =
    		    this.getEntityManager().createQuery("SELECT p FROM Method p", Method.class);
    	List<Method> results = query.getResultList();
    	this.getEntityManager().getTransaction().begin();
    	for (Method method : results) {
			this.getEntityManager().remove(method);
		}
    	this.getEntityManager().getTransaction().commit();
    }
}

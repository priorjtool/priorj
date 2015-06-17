package com.edu.ufcg.splab.priorj.coverage.controller;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.edu.ufcg.splab.priorj.coverage.model.ClassCode;

/**
 * <code>ClassCodeController</code> for database methods abstraction.
 * 
 * @author Berg
 *
 */
public class ClassCodeController extends Controller {

    /**
     * <code>ClassCodeController</code> Contructor.
     */
    public ClassCodeController() {
    	super();
    }
    
    /**
     * Add a classCode object into the database.
     * 
     * @param classCode object.
     */
    public void add(final ClassCode classCode) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().persist(classCode);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Remove an existing classCode Objects.
     * 
     * @param classCode 
     * 			ClassCode object. If the object does not exist nothing happens.
     */
    public void remove(final ClassCode classCode) {
    	this.getEntityManager().getTransaction().begin();
    	this.getEntityManager().remove(classCode);
    	this.getEntityManager().getTransaction().commit();
    }
    
    /**
     * Get an existing classCode object.
     * 	
     * @param classCode 
     * 			ClassCode object.
     * 
     * @return The classCode object. If the object does not exist return null.
     */
    public ClassCode get(final ClassCode classCode) {
    	this.getEntityManager().getTransaction().begin();
    	ClassCode cc = this.getEntityManager().find(ClassCode.class, classCode);
    	this.getEntityManager().getTransaction().commit();
    	return cc;
    	
    }
    
    /**
     * Get all objects in database.
     * 
     * @return List<ClassCode> with all objects presents in database.
     */
    public List<ClassCode> getAll() {
    	TypedQuery<ClassCode> query =
    		    this.getEntityManager().createQuery("SELECT p FROM ClassCode p", ClassCode.class);
    	List<ClassCode> results = query.getResultList();
    	return results;
    }
    
    /**
     * Get the quantoty of all objects presents in database.
     * 
     * @return long with the quantity.
     */
    public long getQuantityObjects() {
    	Query q1 = this.getEntityManager().createQuery("SELECT COUNT(p) FROM ClassCode p");
    	return (long) q1.getSingleResult();
    }
    
    /**
     * Clear database.
     */
    public void clearDb() {
    	TypedQuery<ClassCode> query =
    		    this.getEntityManager().createQuery("SELECT p FROM ClassCode p", ClassCode.class);
    	List<ClassCode> results = query.getResultList();
    	this.getEntityManager().getTransaction().begin();
    	for (ClassCode classCode : results) {
			this.getEntityManager().remove(classCode);
		}
    	this.getEntityManager().getTransaction().commit();
    }
    
}

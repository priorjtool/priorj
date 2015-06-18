package com.edu.ufcg.splab.priorj.coverage.controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Superclass for controller configuration.
 * 
 * @author Berg
 *
 */
public class Controller {

	private EntityManagerFactory emf;
    private EntityManager em;
    
    /**
     * <code>Controller</code> Constructor.
     */
    public Controller() {
    	
    }
    
    public void initDatabase() {
    	emf = Persistence.createEntityManagerFactory("C:/PriorJ/workspace/priorj/com.splab.priorj.plugin/database/coverage.odb");
        em = emf.createEntityManager();
    }
    
    /**
     * Get the entity Manager.
     * 
     * @return	EntityManager object.
     */
    public EntityManager getEntityManager() {
    	return this.em;
    }
    
    public void closeDatabase() {
    	this.em.close();
    	this.emf.close();
    }
}

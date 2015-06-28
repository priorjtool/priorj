package com.edu.ufcg.splab.priorj.coverage.model;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos, Julio H. Rocha
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Class <code>Method</code> is a method that will be covered, each method is formed by a set of <code>Statement</code>.
 * 
 * @author Berg �lisson
 * 
 */
@Entity
public class Method implements Serializable {

	private static final long serialVersionUID = 1L;
	 
	@Id @GeneratedValue
    @Column(name = "ID")
    private long id;
    private String name;
    private List<Statement> statementCoverage;

    /**
	 * Empty Constructor for database requests.
	 */
    public Method() {
    	
    }
    
    /**
     * Constructor class <code>Method</code>
     * @param name  the method name.
     */
    public Method(final String name) {
    	
    	if (name == null)
    		throw new IllegalArgumentException("Null method name!");
    	
    	if (name.isEmpty())
    		throw new IllegalArgumentException("Method with empty name!");
    	
        this.name = name;
        this.statementCoverage = new ArrayList<Statement>();
    }

    /**
     * Get the id in ObjectDB.
     * 
     * @return Long with the database id.
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Add a new object of the type <code>Statement</code> covered by this method.
     * @param sttm - a object of the type <code>Statement</code>.
     */
    public void addStatement(final Statement sttm) {
    	if (sttm == null)
    		throw new IllegalArgumentException("(add statement) Null Statement!");
    	
        statementCoverage.add(sttm);
    }

    /**
     * Get the name of this methods.
     * @return  a string with the method name.
     */
    public String getName() {
        return name;
    }

    /**
     * Send the name of this method.
     * @param name  a string representing the name of the method.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get a list of all the Statements covered by this method, i.e., all lines of code affected by this method.
     * @return a array list of objects of type <code>Statement</code>.
     */
    @ManyToMany
    @JoinTable(name="METHOD_STATEMENT")
    public List<Statement> getStatementCoverage() {
        return statementCoverage;
    }

    /**
     * Send the list of <code>Statement</code> covers this method.
     * @param statementCoverage  a list of objects of type <code>Statement</code>.
     */
    public void setStatementCoverage(final List<Statement> statementCoverage) {
        this.statementCoverage = statementCoverage;
    }

    /**
     * Get list of statements covered by the unique method, i.e., delete all repeats.
     * @return a list of object type <code>Statement</code>.
     */
    public List<Statement> getUniqueStatements() {
        List<Statement> stmts = new ArrayList<Statement>();

        for (Statement stmt : statementCoverage) {
            if (!stmts.contains(stmt)) {
                stmts.add(stmt);
            }
        }

        return stmts;
    }

    /**
     * Obtaining the amount of statements covered by this method, i.e. how many lines of code that copper method.
     * @return an <code>int</code> representing the number of statements covered.
     */
    public int getNumberStatements(){
    	return getStatementCoverage().size();
    }
    
    /**
     * Obtaining the amount of unique statements covered by this method, i.e. how many lines of code that copper method.
     * @return an <code>int</code> representing the number of unique statements covered.
     */
    public int getUniqueNumberStatements(){
    	return getUniqueStatements().size();
    }
   
    /**
     * Compare two objects of type <code>Statement</code>.
     * @param obj a object of type<code> Statement</code>
     * @return returns zero if equal, negative if less, positive if greater
     */
  
	public int compareTo(final Object obj) {
    	if (!(obj instanceof Method)) {
            return -1;
        }
    	
        Method m = (Method) obj;
        
		return getNumberStatements()- m.getNumberStatements();
	}
    
	/**
     * View the data object of type <code>Method</code>.
     * @return the string representing the object.
     */
    public String toString() {
        return name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((statementCoverage == null) ? 0 : statementCoverage
						.hashCode());
		return result;
	}

	public String teste() {
		return "Method [id=" + id + ", name=" + name + ", statementCoverage="
				+ statementCoverage + "]";
	}

	/**
     * Compare two objects of type <code>Method</code> and says if are equal.
     * @param obj  a object of type <code>Method</code>.
     * @return  true or false 
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (statementCoverage == null) {
			if (other.statementCoverage != null)
				return false;
		} else if (!statementCoverage.equals(other.statementCoverage))
			return false;
		return true;
	}

	
}

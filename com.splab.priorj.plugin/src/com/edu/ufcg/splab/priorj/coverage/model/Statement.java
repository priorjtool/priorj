package com.edu.ufcg.splab.priorj.coverage.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos, Julio Henrique Rocha
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

/**
 * The <code>Statement</code> class represents a row covered by the coverage;
 * 
 * @author Berg Élisson
 */
@Entity
public class Statement implements Serializable {

	private static final long serialVersionUID = 1L;
	 
	@Id @GeneratedValue
    @Column(name = "ID")
    private long id;
    private String lineNumber;
	private List<Method> methods;

    /**
	 * Empty Constructor for database requests.
	 */
    public Statement() {
    	
    }
    
    /**
     * Statement class constructor. 
     * @param lineNumber  a string indicating a number line. 
     */
    public Statement(final String lineNumber) {
    	if (lineNumber == null)
    		throw new IllegalArgumentException("Null Statement!");
    	
    	if (lineNumber.isEmpty())
    		throw new IllegalArgumentException("Line Number Empty!");
    	
        this.lineNumber = lineNumber;
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
     * Get the line number that is associated with this statement.
     * @return the line number.
     */
    public String getLineNumber() {
        return lineNumber;
    }

    /**
     * 
     * @param lineNumber
     */
    public void setLineNumber(final String lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    /**
     * Get Methods.
     * 
     * @return Collection<Method>.
     */
    @ManyToMany(mappedBy="statementCoverage")
    public List<Method> getMethods() {
		return methods;
	}

    /**
     * Set Methods.
     * 
     * @param methods
     * 			Collection<Method> methods.
     */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

	/**
     * Compare two objects of type <code>Statement</code> and says if are equal.
     * @param obj  a object of type <code>Statement</code>.
     * @return  true or false 
     */  
    public int compareTo(final Object obj){
    	
    	if (!(obj instanceof Statement))
    		return -1;
    	
    	Statement stmt = (Statement)obj;
    	
    	int thisNumberLine = Integer.parseInt(this.getLineNumber());
    	int objNumberLine =  Integer.parseInt(stmt.getLineNumber());
    	
    	return thisNumberLine - objNumberLine;
    }
    
	/**
     * View the data object of type <code>Statement</code>.
     * @return the string representing the object.
     */
    public String toString() {
        return lineNumber;
//    	return "Statement [id=" + id + ", lineNumber=" + lineNumber + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((lineNumber == null) ? 0 : lineNumber.hashCode());
		return result;
	}

	/**
     * Compare two objects of type <code>Statement</code>.
     * @param obj a object of type<code> Statement</code>
     * @return returns zero if equal, negative if less, positive if greater
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Statement other = (Statement) obj;
		if (id != other.id)
			return false;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		return true;
	}

}

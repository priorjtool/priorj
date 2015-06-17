package com.edu.ufcg.splab.priorj.coverage.model;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Samuel T. C. Santos, Julio H. Rocha.
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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <code>ClassCode</code> represents a class inside the application, 
 * containing the package name, class name and a list of methods found in class.
 * 
 * @author Berg Élisson
 * 
 */
@Entity
public class ClassCode {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue
    private long id;
	private String packageName;
	private String name;
	private List<Method> methodCoverage;
	
	/**
	 * Empty Constructor for database requests.
	 */
	public ClassCode() {
		
	}
	
	/**
	 * <code>ClassCode</code> Constructor 
	 * @param packageName
	 * @param name
	 */
	public ClassCode(final String packageName, final String name){
		this.name = name;
		this.packageName = packageName;
		this.methodCoverage = new ArrayList<Method>();
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
	 *  Adds a new method for this classCode, i.e. means 
	 *  that the class containing that method and they will be seen by the coverage.
	 *  
	 * @param method the method name.
	 */
	public void addMethod(final Method method){
		methodCoverage.add(method);
	}
	
	/**
	 * Get the method name.
	 * 
	 * @return the method name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name to method.
	 * 
	 * @param name a string representing the method name.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Get the package name.
	 * 
	 * @return the package name.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Set a new package a name.
	 * 
	 * @param packageName a string representing the package name.
	 */
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Get the list of methods covered by this class.
	 * 
	 * @return a list of objects type <code>Method</code>.
	 */
	public List<Method> getMethodCoverage() {
		return methodCoverage;
	}
	
	/**
	 * Get the number of covered method by this <code>ClassCode</code>
	 * .
	 * @return a <code>int</code> representing the number of methods.
	 */
	public int getNumberMethodCoverage() {
		return methodCoverage.size();
	}
	

	/**
	 * Set the list of methods covered by this class.
	 * 
	 * @param methodCoverage a list of methods.
	 */
	public void setMethodCoverage(final List<Method> methodCoverage) {
		this.methodCoverage = methodCoverage;
	}
	
	/**
	 * Get the names of all methods found on this class code.
	 * 
	 * @return a list of objects type <code>String</code>.
	 */
	public List<String> getMethodsCoverage() {
		List<String> list = new ArrayList<String>();
		for (Method method : getMethodCoverage()) {
			list.add(toString()+"."+method);
		}
		return list;
	}

    /**
     * Get a list of statements covered by this class code.
     * 
     * @return a list of object type <code>String</code>.
     */
	public List<String> getStatementCoverage(){
		List<String> list = new ArrayList<String>();
		for (Method method : methodCoverage) {
			String statement = "";
			for (Statement sttm : method.getStatementCoverage()) {
				statement = toString() +"."+method+"."+ sttm.toString();
				list.add(statement);
			}
		}
		return list;
	}
	
	/**
	 * Get the number of covered statement by methods of the <code>ClassCode</code>.
	 * 
	 * @return the number of statements.
	 */
	public int getNumberStatemetCoverage(){
		return getStatementCoverage().size();
	}
	
	
    
    /**
     * Compare two objects of type <code>ClassCodet</code>.
     * 
     * @param obj a object of type<code>ClassCode</code>
     * @return returns zero if equal, negative if less, positive if greater
     */
    public int compareTo(final Object obj){
    	        
        ClassCode cc = (ClassCode)obj;
        
    	return this.getNumberMethodCoverage() - cc.getNumberMethodCoverage();
    }

	/**
     * View the data object of type <code>ClassCode</code>.
     * @return the string representing the object.
     */
    public String toString(){
		return packageName +"."+name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result
				+ ((methodCoverage == null) ? 0 : methodCoverage.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		return result;
	}

	public String teste() {
		return "ClassCode [id=" + id + ", packageName=" + packageName
				+ ", name=" + name + ", methodCoverage=" + methodCoverage + "]";
	}

	/**
     * Compare two objects of type <code>Method</code> and says if are equal.
     * is equals if same package name, same name and same numbers of methods coverage.
     * 
     * @param obj  a object of type <code>Method</code>.
     * @return  true or false. 
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassCode other = (ClassCode) obj;
		if (id != other.id)
			return false;
		if (methodCoverage == null) {
			if (other.methodCoverage != null)
				return false;
		} else if (!methodCoverage.equals(other.methodCoverage))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		return true;
	}
	        
}

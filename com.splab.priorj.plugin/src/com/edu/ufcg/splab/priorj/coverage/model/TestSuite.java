package com.edu.ufcg.splab.priorj.coverage.model;

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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <code> TestSuite</code> represents a test suite, i.e., a set of
 * test cases, the package name where the suite is located and the name of the suite.
 * 
 * @author Berg Élisson
 * 
 * @version 1.0
 *
 */
@Entity
public class TestSuite {

	private static final long serialVersionUID = 1L;
	 
    @Id @GeneratedValue
    private long id;
    private String packageName;
    private String name;
    private List<TestCase> testCases;
    
    /**
	 * Empty Constructor for database requests.
	 */
    public TestSuite() {
    	
    }
    
    /**
     * <code> TestSuite</code>  constructor.
     * @param packageName the package name.
     * @param name the suite name.
     */
    public TestSuite(final String packageName, final String name) {
    	if (packageName.isEmpty() || name.isEmpty())
    		throw new IllegalArgumentException("Package or Suite Name Empty!");
    		
        this.packageName = packageName;
        this.name = name;
        this.testCases = new ArrayList<TestCase>();
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
     * Add a test case in the suite.
     * 
     * @param tc a object of type <code>TestCase</code>.
     */
    public void addTestCase(final TestCase tc) { 
    	if (tc == null)
    		return;
    	
        testCases.add(tc);
    }

    /**
     * Get the suite name.
     * 
     * @return the suite name.
     */
    public String getName() {
        return name;
    }

    /**
     * Send a name to suite.
     * 
     * @param name a <code>String</code> representing the suite name.
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
     * Set the package name.
     * 
     * @param packageName the package name.
     */
    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    /**
     * Get the list of <code>TestCase</code> in this <code>TestSuite</code>.
     * 
     * @return the list of <code>TesteCase</code>.
     */
    public List<TestCase> getTestCases() {
        return testCases;
    }

    /**
     * Set the list of test cases to test suite.
     * 
     * @param testCases a list of objects type <code>TestCase</code>.
     */
    public void setTestCases(final List<TestCase> testCases) {
        this.testCases = testCases;
    }


    /**
     * Get the numbers of Test Suites.
     * 
     * @return the number of test suites.
     */
    public int getNumberOfSuites() {
        return testCases.size();
    }
    
    /**
     * Compare two objects of type <code>TestSuite</code>.
     * 
     * @param obj a object of type<code> Statement</code>
     * 
     * @return returns zero if equal, negative if less, positive if greater
     */
    public int compareTo(final TestSuite suite){
    	return getNumberOfSuites() - suite.getNumberOfSuites();
    }
    
    /**
     * View the data object of type <code>TestSuite</code>.
     * @return the <code>String</code> representing the object.
     */
    public String toString() {
        return packageName + "." + name;
    }
    
    public String teste() {
    	return "TestSuite [id=" + id + ", packageName=" + packageName
				+ ", name=" + name + ", testCases=" + testCases + "]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((testCases == null) ? 0 : testCases.hashCode());
		return result;
	}

	/**
     * Compare two objects of type <code>TestSuite</code> and says if are equal.
     * 
     * @param obj  a object of type <code>Statement</code>.
     * 
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
		TestSuite other = (TestSuite) obj;
		if (id != other.id)
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
		if (testCases == null) {
			if (other.testCases != null)
				return false;
		} else if (!testCases.equals(other.testCases))
			return false;
		return true;
	}

}

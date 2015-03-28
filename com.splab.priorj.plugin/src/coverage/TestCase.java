package coverage;

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

import java.util.ArrayList;
import java.util.List;

/**
 * The class <code>TesteCase</code> represents a test case contains a name and a list of
 * classes covered by the tests.
 * 
 * @author Samuel Santos
 * @author Julio Henrique
 */
public class TestCase {

    private String name = "";
    private List<ClassCode> classCoverage;
    private int numberStatementsCovered;
    
	private int numberMethodsCovered;
    private String signature = "";


    /**
     * <code>TesteCase</code> constructor.
     * @param name the test case name.
     */
    public TestCase(String name) {
        this.name = name;
        this.classCoverage = new ArrayList<ClassCode>();
        numberMethodsCovered = 0;
        numberStatementsCovered = 0;
    }

    /**
     * Add a object of type <code>ClassCode</code> for the test case.
     * @param clazz a object <code>ClassCode</code>.
     */
    public void addClassCoverage(ClassCode clazz) {    
        classCoverage.add(clazz);
    }
    
    public String getSignature(){
    	return this.signature;
    }
    
    public void setSignature(String signature){
    	this.signature = signature;
    }
    
    /**
     * Get the test case name.
     *  
     * @return the test case name.
     */
    public String getName() {
        return name;
    }

    /**
     * Send a name for the test case.
     * @param name	 a string representing the test case name.
     */
    public void setName(String name) { 
        this.name = name;
    }

    /**
     * Obtain all classes code covered by this test case.
     * 
     * @return a list of <code>ClassCode</code>
     */
    public List<ClassCode> getClassCoverage() { 
        return classCoverage;
    }

    /**
     * Set the list of classes code covered by this test case.
     * 
     * @param methodsCoverage a list of <code>ClassCode</code>.
     */
    public void setClassCoverage(List<ClassCode> methodsCoverage) {
        this.classCoverage = methodsCoverage;
    }

    /**
     * Get the list of  <code>ClassCode</code> name covered.
     * 
     * @return a list of <code>String</code> with <code>ClassCode</code> names. 
     */
    public List<String> getClassCodeCoverage() {
        
        List<String> list = new ArrayList<String>();
        
        for (ClassCode clazz : classCoverage) {
            
            list.add(clazz.toString());
        }
        
        return list;
    }

    /**
     * Get the list of methods name covered by test case.
     * 
     * @return a list of <code>String</code> with methods name.
     */
    public List<String> getMethodCoverage() {
        
        List<String> list = new ArrayList<String>();
        
        for (ClassCode clazz : classCoverage) {
            
            for (String string : clazz.getMethodsCoverage()) {
                list.add(string);
            }
        }
        
        return list;
    }
    
    public List<String> getMethodCoverageDistinct() {
        
        List<String> list = new ArrayList<String>();
        
        for (ClassCode clazz : classCoverage) {
            
            for (String string : clazz.getMethodsCoverage()) {
                if (!list.contains(string))
                	list.add(string);
            }
        }
        
        return list;
    }

    /**
     * Get the number of covered methods.
     * 
     * @return the number of covered methods.
     */
    public int getNumberMethodCoverage(){
    	return getMethodCoverage().size();
    }
    
    /**
     * Get the list of <code>Statement</code> covered by test case.
     * 
     * @return a list of <code>String</code> representing covered statements.
     * 
     */
    public List<String> getStatementsCoverage() {
        
        List<String> lista = new ArrayList<String>();
        
        for (ClassCode clazz : classCoverage) {
            
            for (String string : clazz.getStatementCoverage()) {
                lista.add(string);
            }
        }
        
        return lista;
    }
    
    /**
     * Get the number of covered statements.
     * 
     * @return the number of covered statements.
     */
    public int getNumberStatementsCoverage(){
    	return getStatementsCoverage().size();
    }
    
    /**
     * Get the list of  unique covered statements.
     * 
     * @return a list of <code>String</code> representing number lines.
     */
    public List<String> getStatementsCoverageDistinct() {
        
        List<String> lista = new ArrayList<String>();
        
        for (ClassCode clazz : classCoverage) {
            
            for (String string : clazz.getStatementCoverage()) {
            	if(!lista.contains(string))
            		lista.add(string);
            }
        }
        
        return lista;
    }

    /**
     * Get the numbers of statements distinct.
     * 
     * @return the number of statements distinct.
     */
    public int getNumberStatementsCoverageDistinct(){
    	return numberStatementsCovered != 0 ? numberStatementsCovered : getStatementsCoverageDistinct().size();
    }
    
    public int getNumberMethodsCoveredDistinct(){
    	return numberMethodsCovered != 0 ? numberMethodsCovered : getMethodCoverageDistinct().size();
    }
    
    public void setNumberStatementsCoverageDistinct( int numberStatements){
    	numberStatementsCovered = numberStatements;
    }
    
    public void setNumberMethodsCoveredDistinct(int numberMethods){
    	numberMethodsCovered = numberMethods;
    }
    
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classCoverage == null) ? 0 : classCoverage.hashCode());
		
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (classCoverage == null) {
			if (other.classCoverage != null)
				return false;
		} else if (!classCoverage.equals(other.classCoverage))
			return false;
	
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

    /**
     * Compare two objects of type <code>ClassCodet</code>.
     * 
     * @param obj a object of type<code>ClassCode</code>
     * 
     * @return returns zero if equal, negative if less, positive if greater
     */
    public int compareTo(TestCase tc){
    	return getNumberMethodCoverage() - tc.getNumberMethodCoverage();
    }


	/**
     * View the data object of type <code>ClassCode</code>.
     * 
     * @return the string representing the object.
     */
    public String toString() {        
        return name;
    }

}

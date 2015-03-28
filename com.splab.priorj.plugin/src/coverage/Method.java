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
 * Class <code>Method</code> is a method that will be covered, each method is formed by a set of <code>Statement</code>.
 * 
 * @author Samuel Santos
 * @author Julio Henrique
 * 
 */
public class Method {

    private String name;
    private List<Statement> statementCoverage;

    /**
     * Constructor class <code>Method</code>
     * @param name  the method name.
     */
    public Method(String name) {
    	
    	if (name == null)
    		throw new IllegalArgumentException("Null method name!");
    	
    	if (name.isEmpty())
    		throw new IllegalArgumentException("Method with empty name!");
    	
        this.name = name;
        this.statementCoverage = new ArrayList<Statement>();
    }

    /**
     * Add a new object of the type <code>Statement</code> covered by this method.
     * @param sttm - a object of the type <code>Statement</code>.
     */
    public void addStatement(Statement sttm) {
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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get a list of all the Statements covered by this method, i.e., all lines of code affected by this method.
     * @return a array list of objects of type <code>Statement</code>.
     */
    public List<Statement> getStatementCoverage() {
        return statementCoverage;
    }

    /**
     * Send the list of <code>Statement</code> covers this method.
     * @param statementCoverage  a list of objects of type <code>Statement</code>.
     */
    public void setStatementCoverage(List<Statement> statementCoverage) {
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
  
	public int compareTo(Object obj) {
    	if (!(obj instanceof Method)) {
            return -1;
        }
    	
        Method m = (Method) obj;
        
		return getNumberStatements()- m.getNumberStatements();
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
		if (!(obj instanceof Method))
			return false;
		Method other = (Method) obj;
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

	/**
     * View the data object of type <code>Method</code>.
     * @return the string representing the object.
     */
    public String toString() {
        return name;
    }

	
}

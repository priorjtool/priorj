package coverage;


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
 * @author Samuel Santos
 * @author Julio Henrique
 * 
 */
public class Statement {

    private String lineNumber;

    /**
     * Statement class constructor. 
     * @param lineNumber  a string indicating a number line. 
     */
    public Statement(String lineNumber) {
    	if (lineNumber == null)
    		throw new IllegalArgumentException("Null Statement!");
    	
    	if (lineNumber.isEmpty())
    		throw new IllegalArgumentException("Line Number Empty!");
    	
        this.lineNumber = lineNumber;
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
    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    /**
     * Compare two objects of type <code>Statement</code> and says if are equal.
     * @param obj  a object of type <code>Statement</code>.
     * @return  true or false 
     */  
    public int compareTo(Object obj){
    	
    	if (!(obj instanceof Statement))
    		return -1;
    	
    	Statement stmt = (Statement)obj;
    	
    	int thisNumberLine = Integer.parseInt(this.getLineNumber());
    	int objNumberLine =  Integer.parseInt(stmt.getLineNumber());
    	
    	return thisNumberLine - objNumberLine;
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
		if (!(obj instanceof Statement))
			return false;
		Statement other = (Statement) obj;
		if (lineNumber == null) {
			if (other.lineNumber != null)
				return false;
		} else if (!lineNumber.equals(other.lineNumber))
			return false;
		return true;
	}
	
	/**
     * View the data object of type <code>Statement</code>.
     * @return the string representing the object.
     */
    public String toString() {
        return lineNumber;
    }

}

package com.edu.ufcg.splab.priorj.report;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2014  SPLab
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

import com.edu.ufcg.splab.priorj.coverage.model.ClassCode;
import com.edu.ufcg.splab.priorj.coverage.model.Method;
import com.edu.ufcg.splab.priorj.coverage.model.Statement;
import com.edu.ufcg.splab.priorj.coverage.model.TestCase;
import com.edu.ufcg.splab.priorj.coverage.model.TestSuite;

/**
 * This class generate a simple coverage report.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0
 * 
 */
public class GenerateChangesCoveredReport {
    
    /**
     * Get the coverage report generated.
     * 
     * @return
     *      The coverage report.
     */
	public static String generateCoverageReport(final List<String> affectedBlocks){
		StringBuilder script = new StringBuilder();
		script.append("var changeList = new Array();\n\n");
		script.append("$(function(){\n");
		script.append("\tloadChangeList();\n");
		script.append("});\n\n");
		script.append("function loadChangeList(){\n");
		for (String change : affectedBlocks){
			script.append("\tchangeList.push('" + change + "');\n");
		}
		script.append("}\n\n");
		script.append("function getChangeList(){\n");
		script.append("\treturn changeList;\n");
		script.append("}\n");
		return script.toString();
	}

}
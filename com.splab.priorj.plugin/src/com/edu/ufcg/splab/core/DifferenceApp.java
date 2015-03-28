package com.edu.ufcg.splab.core;


/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2013  Julio Henrique Rocha
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
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


/**
 * DifferenceApp class
 * 
 * @author Julio H. Rocha
 *
 */
public class DifferenceApp {

    private File pathNewCode;

    private File pathOldCode;

    private List<String> listDiff;
    
    private final String slash = System.getProperty("file.separator");

    public DifferenceApp(String pathNewCode, String pathOldCode) {
        this.pathNewCode = new File(pathNewCode);
        this.pathOldCode = new File(pathOldCode);
        listDiff = new ArrayList<String>();
    }

    public void run() throws Exception {
        
        checkDifference(pathNewCode, pathOldCode);
    }

    private void checkDifference(File pathNew, File pathOld) throws Exception {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return !name.contains("Exception")
                && !name.contains("Test") && !name.contains("PriorJ");
            }
        };
        
        File[] children = pathNew.listFiles(filter);
        
        for (File arq : children) {
            
            if (arq.isDirectory()) {
                String arquivo = arq.getName();
                checkDifference(new File(pathNew + slash + arquivo), new File(pathOld + slash + arquivo));
            } else if (arq.getName().endsWith(".java")) {
            	if(!pathNew.exists() || !pathOld.exists()){
            		throw new Exception("File "+ pathOld+" not found in old code.");
            	}
                Difference dif = new Difference(pathNew.toString() + slash + arq.getName(), pathOld.toString() + slash + arq.getName());
                dif.diff();
                List<String> affected = dif.getStatementsDiff();
                if (affected.size() > 0) {
                    listDiff.addAll(affected);
                }
            }
        }
    }

    public List<String> getListDiff() {
        return listDiff;
    }
    

    public void setListDiff(List<String> listDiff) {    
        this.listDiff = listDiff;
    }
        
}

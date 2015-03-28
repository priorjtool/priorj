package com.edu.ufcg.splab.core;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2012-2014 SPLab
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
import japa.parser.ASTHelper;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.WhileStmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 		This class instrument a class code to count the line number
 * 		to found the statement line number.
 * </p>
 * @author Samuel T. C. Santos
 * @author Julio H. Rocha
 * @author Igor Meira
 * 
 * @version 1.0
 *
 */
public class InstrumentClass {

    private String nameField = "watchPriorJApp";
    
    private String pathFile;

    private String nameFile;
    
    private int watchNumber;

    private boolean isTest;
    
    /**
     * Instrument class Constructor, this class instrument source code and tests.
     * 
     * @param pathFile
     * 	 the path to file.
     * @param nameFile
     * 	 the file name.
     * @param isTest
     *   true if the file is a test case or false otherwise.
     */
    public InstrumentClass(String pathFile, String nameFile, boolean isTest) {
        this.pathFile = pathFile;
        this.nameFile = nameFile;
        watchNumber = 0;
        this.isTest = isTest;
    }

    /**
     * Getting the file path.
     * @return
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     * Setting path to this file.
     * @param pathFile
     */
    public void setPathFile(String pathFile) {
        
        this.pathFile = pathFile;
    }

    /**
     * 
     * @return
     */
    public String getNameFile() {
        
        return nameFile;
    }

    /**
     * 
     * @param nameFile
     */
    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    /**
     * 
     * @param nameField
     */
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    /**
     * 
     * @return
     */
    public String getNameField() {
        
        return nameField;
    }

    /**
     * 
     * @throws Exception
     */
    public void instrumentationRun() throws Exception {
        
        FileInputStream in = new FileInputStream(getPathFile() + getNameFile());
        
        CompilationUnit compilation;
        
        compilation = JavaParser.parse(in);
        
        in.close();
        
        if(!isTest){
        	instrumentarClasse(compilation);
        }
        
        changeMethods(compilation);
        
        addImportDeclaration(compilation);
        
        writeFile(getPathFile(), getNameFile(), compilation.toString());
    }

	private void addImportDeclaration(CompilationUnit compilation) {
		
		ImportDeclaration importDec = null;
		
		if (isTest){
			importDec = new ImportDeclaration(ASTHelper.createNameExpr("com.edu.ufcg.splab.coverage.annotations.TestCaseDeclaration"), false, false);
		}
		else{
			importDec = new ImportDeclaration(ASTHelper.createNameExpr("com.edu.ufcg.splab.coverage.annotations.MethodDeclaration"), false, false);
		}
		List<ImportDeclaration> imports  = new  ArrayList<ImportDeclaration>();
		if (compilation.getImports() != null){
			imports = compilation.getImports();
		}
        imports.add(importDec);
        compilation.setImports(imports);
	}

    /**
     * 
     * @param compilation
     */
    public void instrumentarClasse(CompilationUnit compilation) {
        
        FieldDeclaration vigia = ASTHelper.createFieldDeclaration(ModifierSet.STATIC, ASTHelper.BOOLEAN_TYPE, getNameField());
        
        ClassOrInterfaceDeclaration classe = null;
        
        List<TypeDeclaration> types = compilation.getTypes();
        
        for (TypeDeclaration typeDeclaration : types) {
            
            if (typeDeclaration instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration) typeDeclaration).isInterface()) {
                classe = (ClassOrInterfaceDeclaration) typeDeclaration;
            }
        }
        
        if (classe == null) return;
        
        List<BodyDeclaration> membros = classe.getMembers();
        
        if (membros.contains(vigia)) {   
            return;
        }
        for (BodyDeclaration bodyDeclaration : membros) {
        	
            if (bodyDeclaration instanceof MethodDeclaration) {
                instrumentaMetodo((MethodDeclaration) bodyDeclaration);
            }else if(bodyDeclaration instanceof ConstructorDeclaration){
            	instrumentaConstrututor((ConstructorDeclaration) bodyDeclaration);
            }
            else if (bodyDeclaration instanceof ClassOrInterfaceDeclaration){
            	
            	ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) bodyDeclaration;
            	
                List<BodyDeclaration> members =  clazz.getMembers();
                
                for (BodyDeclaration member : members) {
                	if (member instanceof MethodDeclaration ){
                        instrumentaMetodo((MethodDeclaration) member);
                	}
                	else if (member instanceof ConstructorDeclaration){
                		instrumentaConstrututor((ConstructorDeclaration) member);
                	}
                }
            
            }
        }
        
        ASTHelper.addMember(classe, vigia);
    }

	private void changeMethods(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    changeMethod(method);
                }
            }
        }
    }

    private void changeMethod(MethodDeclaration n) {
        // Adding our Annotation to Method Coverage
    	List<AnnotationExpr> annotations = new ArrayList<AnnotationExpr>();
    	
    	if(isTest){
    		boolean foundTestAnnotation = false;//searching JUnit 4 annotations
        	if(n.getAnnotations() !=null){
        		if (!n.getAnnotations().isEmpty()){
        			annotations = n.getAnnotations();
        			for (AnnotationExpr a : annotations){
        				if (a != null){
    	    				if ( a.getName().toString().equals("Test") ){
    	    					foundTestAnnotation = true;
    	    					break;
    	    				}
        				}
        			}
        		}
        	}
        	
        	String regex = "test.*"; //searching only for JUnit 3 tests
	    	if (n.getName().matches(regex)){
	    		markWithAnnotation(n, annotations);
	    	}
	    	else {
	    		if (foundTestAnnotation){
	    			markWithAnnotation(n, annotations);
	    		}
	    	}
    	}
    	else{ // instrument simple method
    		markWithAnnotation(n, annotations);
    	}
    }

	private void markWithAnnotation(MethodDeclaration n,
		List<AnnotationExpr> annotations) {
		NameExpr annName = null;
    	if (isTest){
    		annName = new NameExpr("TestCaseDeclaration");
    	}
    	else{
    		annName = new NameExpr("MethodDeclaration");
    	}
    	AnnotationExpr ae = new MarkerAnnotationExpr(annName);
    	annotations.add(ae);
    	n.setAnnotations(annotations);
	}
	
    /**
     * 
     * @param contrutor
     */
   public void instrumentaConstrututor(ConstructorDeclaration contrutor) {
        
        BlockStmt linhas = contrutor.getBlock();
        
        if (linhas == null) return;
        
        List<Statement> stmts = linhas.getStmts();
        
        FieldAccessExpr acessoAtributo = new FieldAccessExpr(new ThisExpr(null), getNameField());
        
        NameExpr valorExpressao = new NameExpr(acessoAtributo.getField());
        
        AssignExpr expressao = new AssignExpr(valorExpressao, valorExpressao, AssignExpr.Operator.assign);
        
        Statement watchStatement = new ExpressionStmt(expressao);
        
        List<Statement> novaLista = new ArrayList<Statement>();
        
        if (stmts != null) {
            for (int i = 0; i < stmts.size(); i++) {
                Statement stm = stmts.get(i);
              
                if(i != 0 || !(stmts.get(i).toString().contains("super") || stmts.get(i).toString().contains("this")) )
                	novaLista.add(watchStatement);
                stm = instrumentarBody(stm, watchStatement);
                novaLista.add(stm);
                incrementWatch();
            }
            
            linhas.setStmts(novaLista);
        }
    }
    /**
     * 
     * @param metodo
     */
    public void instrumentaMetodo(MethodDeclaration metodo) {
        
        BlockStmt linhas = metodo.getBody();
        
        if (linhas == null) return;
        
        List<Statement> stmts = linhas.getStmts();
        
        FieldAccessExpr acessoAtributo = new FieldAccessExpr(new ThisExpr(null), getNameField());
        
        NameExpr valorExpressao = new NameExpr(acessoAtributo.getField());
        
        AssignExpr expressao = new AssignExpr(valorExpressao, valorExpressao, AssignExpr.Operator.assign);
        
        Statement watchStatement = new ExpressionStmt(expressao);
        
        List<Statement> novaLista = new ArrayList<Statement>();
        
        if (stmts != null) {
            
            for (int i = 0; i < stmts.size(); i++) {
                Statement stm = stmts.get(i);
                novaLista.add(watchStatement);
                stm = instrumentarBody(stm, watchStatement);
                novaLista.add(stm);
                incrementWatch();
            }
            
            
            linhas.setStmts(novaLista);
        }
    }

    /**
     * Increment the total number line covered.
     */
	private void incrementWatch() {
		watchNumber++;
	}

    /**
     * 
     * @param stm
     * @param watch
     * @return
     */
    public Statement instrumentarBody(Statement stm, Statement watch) {
        
        if (stm == null) {
            return null;
        } else if (stm instanceof BlockStmt) {
            
            List<Statement> novaLista = new ArrayList<Statement>();
            
            BlockStmt novoBloco = (BlockStmt) stm;
            
            if(novoBloco.getStmts() != null){
            	for (Statement stat : novoBloco.getStmts()) {
            		novaLista.add(watch);
            		incrementWatch();
            		novaLista.add(instrumentarBody(stat, watch)); 
            	}          	
            	
            }
            novoBloco.setStmts(novaLista);
      
            return novoBloco;
        } else if (stm instanceof IfStmt) {
            
            IfStmt state = (IfStmt) stm;
            
            state.setThenStmt(instrumentarBody(state.getThenStmt(), watch));
            
            state.setElseStmt(instrumentarBody(state.getElseStmt(), watch));
            
            
            return state;
        } else if (stm instanceof WhileStmt) {
            
            WhileStmt state = (WhileStmt) stm;
            
            state.setBody(instrumentarBody(state.getBody(), watch));
            
            return state;
        } else if (stm instanceof ForStmt) {
            
            ForStmt state = (ForStmt) stm;
            
            state.setBody(instrumentarBody(state.getBody(), watch));
            
            return state;
        } else if (stm instanceof ForeachStmt) {
            
            ForeachStmt state = (ForeachStmt) stm;
            
            state.setBody(instrumentarBody(state.getBody(), watch));
            
            return state;
        } else if (stm instanceof TryStmt) {
            
            TryStmt state = (TryStmt) stm;
            
            state.setTryBlock((BlockStmt) instrumentarBody(state.getTryBlock(), watch));
            
            state.setFinallyBlock((BlockStmt) instrumentarBody(state.getFinallyBlock(), watch));
            
            return state;
        } else if (stm instanceof DoStmt) {
        	
        	DoStmt state = (DoStmt) stm;
        	
        	state.setBody(instrumentarBody(state.getBody(), watch));
        	
        	return state;        	
        } else {     
            return stm;
        }
    }
    


    /**
     * 
     * @param dir
     * @param nameFile
     * @param data
     * @throws IOException
     */
    public void writeFile(String dir, String nameFile, String data) throws IOException {
        
        File path = new File(dir);
        
        if (!path.exists()) path.mkdir();
        
        FileOutputStream out = new FileOutputStream(new File(dir + nameFile));
        
        out.write(data.getBytes());
        
        out.close();
    }

    /**
     * This method get the number of line founded in the file.
     * 
     * @return
     * 		Number of statements.
     */
	public int getWatchNumber() {
		return watchNumber;
	}

}
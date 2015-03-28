package com.edu.ufcg.splab.core;

/*
* PriorJ: JUnit Test Case Prioritization.
* 
* Copyright (C) 2011-2012  Julio Henrique
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
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.type.Type;

import java.io.FileInputStream;

import java.util.ArrayList;

import java.util.List;



public class Difference {

    private List<String> fieldDiff;

    private List<String> statementsDiff;

    CompilationUnit compNormal;

    CompilationUnit compOld;

    private String className;

    public String getClassName() {
        
        return className;
    }

    public void setClassName(String className) {

        this.className = className;
    }

    public Difference(String path, String pathOld) {
        FileInputStream normal, modified;
        try {
            normal = new FileInputStream(path);
            modified = new FileInputStream(pathOld);
            compNormal = JavaParser.parse(normal);
            compOld = JavaParser.parse(modified);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fieldDiff = new ArrayList<String>();
        this.statementsDiff = new ArrayList<String>();
    }

    public List<String> getStatementsDiff() {
        
        return statementsDiff;
    }

    public void setStatementsDiff(List<String> statementsDiff) {
        
        this.statementsDiff = statementsDiff;
    }

    public boolean contaisFieldInDiff(String str) {
        
        return fieldDiff.contains(str);
    }

    public void diff() {
        
        List<BodyDeclaration> normalFields = new ArrayList<BodyDeclaration>();
        
        List<BodyDeclaration> modFields = new ArrayList<BodyDeclaration>();
        
        List<MethodDeclaration> normalMethods = new ArrayList<MethodDeclaration>();
        
        List<MethodDeclaration> modMethods = new ArrayList<MethodDeclaration>();
        
        /**
         * List to constructor normal in the old version
         */
        List<ConstructorDeclaration> normalConstructor = new ArrayList<ConstructorDeclaration>();
        /**
         * List to constructor modified in the new version
         */
        List<ConstructorDeclaration> modConstructor = new ArrayList<ConstructorDeclaration>();
        
        
        //handler the constructor

        for (TypeDeclaration typeDeclaration : compNormal.getTypes()) {
            setClassName(compNormal.getPackage().getName() +"."+typeDeclaration.getName());
            List<BodyDeclaration> body = typeDeclaration.getMembers();
            for (BodyDeclaration bodyDeclaration : body) {
                if (bodyDeclaration instanceof FieldDeclaration)
                	normalFields.add(bodyDeclaration); 
                else if (bodyDeclaration instanceof ConstructorDeclaration) {
                    normalConstructor.add((ConstructorDeclaration) bodyDeclaration);
                }
            }
        }
        
        for (TypeDeclaration typeDeclaration : compOld.getTypes()) {
            
            List<BodyDeclaration> body = typeDeclaration.getMembers();
            
            for (BodyDeclaration bodyDeclaration : body) {
                if (bodyDeclaration instanceof FieldDeclaration)
                	modFields.add(bodyDeclaration);
                else if (bodyDeclaration instanceof ConstructorDeclaration)
                	modConstructor.add((ConstructorDeclaration) bodyDeclaration);
            }
        }
        
        for (TypeDeclaration typeDeclaration : compNormal.getTypes()) {
            setClassName(compNormal.getPackage().getName() +"."+typeDeclaration.getName());
            List<BodyDeclaration> body = typeDeclaration.getMembers();
            for (BodyDeclaration bodyDeclaration : body) {
                if (bodyDeclaration instanceof FieldDeclaration) normalFields.add(bodyDeclaration); else if (bodyDeclaration instanceof MethodDeclaration) {
                    normalMethods.add((MethodDeclaration) bodyDeclaration);
                }
            }
        }
        
        for (TypeDeclaration typeDeclaration : compOld.getTypes()) {
            
            List<BodyDeclaration> body = typeDeclaration.getMembers();
            
            for (BodyDeclaration bodyDeclaration : body) {
                if (bodyDeclaration instanceof FieldDeclaration) modFields.add(bodyDeclaration); else if (bodyDeclaration instanceof MethodDeclaration) modMethods.add((MethodDeclaration) bodyDeclaration);
            }
        }

        // verify the constructor differences
        diffConstructors(normalConstructor, modConstructor);
        diffMethods(normalMethods, modMethods);
    }

    private void diffConstructors(List<ConstructorDeclaration> normal, List<ConstructorDeclaration> mod) {
        
        for (ConstructorDeclaration constructorDeclaration : normal) {
            
            checkConstructors(constructorDeclaration, mod);
        }
    }
    
  private void checkConstructors(ConstructorDeclaration constructor, List<ConstructorDeclaration> lista) {
        
    	boolean encontrou = false;
        for (ConstructorDeclaration constr : lista) {
            String assinaturaMethod = generateAssignatureConstructor(constructor);
            
            String assinaturaConstr = generateAssignatureConstructor(constr);
            
            if (assinaturaMethod.equals(assinaturaConstr)) {
            	encontrou = true;
                boolean isDiff = isDiffConstructors(constructor.getBlock(), constr.getBlock());
                if (isDiff) {
                    List<String> lines = new ArrayList<String>();
                    for (Statement stt : constructor.getBlock().getStmts()) {
                        lines.addAll(getAllLineStatementsMethod(stt, constructor.getName()));
                    }
                    statementsDiff.addAll(lines);
                }
            }
        }
        if(!encontrou){
        	List<String> lines = new ArrayList<String>();
            for (Statement stt : constructor.getBlock().getStmts()) {
                lines.addAll(getAllLineStatementsMethod(stt, constructor.getName()));
            }
            statementsDiff.addAll(lines);
        }
    }
    

    
    private void diffMethods(List<MethodDeclaration> normal, List<MethodDeclaration> mod) {
        
        for (MethodDeclaration methodDeclaration : normal) {
            
            checkMethods(methodDeclaration, mod);
        }
    }

    private void checkMethods(MethodDeclaration method, List<MethodDeclaration> lista) {
        
    	boolean encontrou = false;
        for (MethodDeclaration mt : lista) {
            String assinaturaMethod = generateAssignature(method);
            
            String assinaturaMt = generateAssignature(mt);
            
            if (assinaturaMethod.equals(assinaturaMt)) {
            	encontrou = true;
                boolean isDiff = isDiffMethods(method.getBody(), mt.getBody());
                if (isDiff) {
                    List<String> lines = new ArrayList<String>();
                    for (Statement stt : method.getBody().getStmts()) {
                        lines.addAll(getAllLineStatementsMethod(stt, method.getName()));
                    }
                    statementsDiff.addAll(lines);
                }
            }
        }
        if(!encontrou){
        	List<String> lines = new ArrayList<String>();
            for (Statement stt : method.getBody().getStmts()) {
                lines.addAll(getAllLineStatementsMethod(stt, method.getName()));
            }
            statementsDiff.addAll(lines);
        }
    }
    
  private String generateAssignatureConstructor(ConstructorDeclaration constructor) {
    
        String assignature = constructor.getName();;
        
        List<Parameter> parametros = constructor.getParameters();
        
        if (parametros != null) {
            
            for (Parameter param : constructor.getParameters()) {
                assignature += param.getType();
            }
        }
        
        return assignature;
    }

    private String generateAssignature(MethodDeclaration method) {
        
        String name = method.getName();
       
        Type type = method.getType();
        String assignature = type+name;
        
        List<Parameter> parametros = method.getParameters();
        
        if (parametros != null) {
            
            for (Parameter param : method.getParameters()) {
                assignature += param.getType();
            }
        }
        
        return assignature;
    }

    private List<String> getAllLineStatementsMethod(Statement state, String assinaturaMethod) {
        
        List<String> stmts = new ArrayList<String>();
        
        if (state == null) return stmts;
        
        if (state instanceof BlockStmt) {
            
            BlockStmt bloco = (BlockStmt) state;
            
            for (Statement st : bloco.getStmts()) {
                stmts.addAll(getAllLineStatementsMethod(st, assinaturaMethod));
            }
        } else if (state instanceof IfStmt) {
            
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
            stmts.add(line);
            
            IfStmt ifSt = (IfStmt) state;
            
            stmts.addAll(getAllLineStatementsMethod(ifSt.getThenStmt(),assinaturaMethod));
            
            stmts.addAll(getAllLineStatementsMethod(ifSt.getElseStmt(), assinaturaMethod));
        } else if (state instanceof ForStmt) {
            
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
        	stmts.add(line);
            
            ForStmt forSt = (ForStmt) state;
            
            stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), assinaturaMethod));
        } else if (state instanceof ForeachStmt) {
            
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
            stmts.add(line);
            
            ForeachStmt forSt = (ForeachStmt) state;
            
            stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), assinaturaMethod));
        } else if (state instanceof WhileStmt) {
            
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
            stmts.add(line);
            
            WhileStmt forSt = (WhileStmt) state;
            
            stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), assinaturaMethod));
        } else if (state instanceof TryStmt) {
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
            stmts.add(line);
            
            TryStmt trySt = (TryStmt) state;
            
            stmts.addAll(getAllLineStatementsMethod(trySt.getTryBlock(), assinaturaMethod));
            
            stmts.addAll(getAllLineStatementsMethod(trySt.getFinallyBlock(), assinaturaMethod));
        } else {
            
        	String line = getClassName() +"."+assinaturaMethod+"()."+ state.getBeginLine();
            
            if (!state.toString().contains("watchPrior")) {
                stmts.add(line);
            }
        }
        
        return stmts;
    }

    private boolean isDiffConstructors(BlockStmt blockNormal, BlockStmt blockMod) {
    	return isDiffMethods(blockNormal, blockMod);
    }

    
    private boolean isDiffMethods(BlockStmt blockNormal, BlockStmt blockMod) {
        
        List<Statement> stNormal = null;
        
        List<Statement> stMod = null;
        
        if (blockNormal != null) stNormal = blockNormal.getStmts();
        
        if (blockMod != null) stMod = blockMod.getStmts();
        
        if (stNormal == stMod) return false;
        
        if (stNormal.size() != stMod.size()) return true;
        
        for (int i = 0; i < stNormal.size(); i++) {
            
            if (!isEqualsStatement(stNormal.get(i), stMod.get(i))) return true;
        }
        
        return false;
    }

    private boolean isEqualsStatement(Statement normalSt, Statement modSt) {
        
        return normalSt.toString().equals(modSt.toString());
    }

}

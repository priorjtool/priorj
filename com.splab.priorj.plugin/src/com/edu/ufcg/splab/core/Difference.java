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

	private static final String DOT 				= ".";
	private static final String COMMA				= ",";
	private static final String PARENTHESES			= "()";
	private static final String PARENTHESES_OPEN	= "(";
	private static final String PARENTHESES_CLOSED	= ")";
	
	private List<String> fieldDiff;
	private List<String> blockDiff;
	private List<String> statementDiff;

	CompilationUnit compChanged;
	CompilationUnit compOld;

	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Take the difference between two Java files.
	 * 
	 * @param changedFilePath
	 * 			<String> With the changed file path.
	 * @param originalFilePath
	 * 			<String> With the original file path.
	 */
	public Difference(final String changedFilePath, final String originalFilePath) {
		FileInputStream changed, old;
		try {
			changed = new FileInputStream(changedFilePath);
			old = new FileInputStream(originalFilePath);
			compChanged = JavaParser.parse(changed);
			compOld = JavaParser.parse(old);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.fieldDiff = new ArrayList<String>();
		this.blockDiff = new ArrayList<String>();
		this.statementDiff = new ArrayList<String>();
	}

	public List<String> getBlockDiff() {
		return blockDiff;
	}
	
	public List<String> getStatementDiff() {
		return statementDiff;
	}

	public void setBlockDiff(List<String> blockDiff) {
		this.blockDiff = blockDiff;
	}

	public boolean contaisFieldInDiff(String str) {
		return fieldDiff.contains(str);
	}

	public void diff() {
		/**
		 * Lists to the changed code version.
		 */
		List<ConstructorDeclaration> changedConstructor = new ArrayList<ConstructorDeclaration>();
		List<BodyDeclaration> changedFields = new ArrayList<BodyDeclaration>();
		List<MethodDeclaration> changedMethods = new ArrayList<MethodDeclaration>();
		
		/**
		 * Lists to the old code version.
		 */
		List<ConstructorDeclaration> originalConstructor = new ArrayList<ConstructorDeclaration>();
		List<BodyDeclaration> originalFields = new ArrayList<BodyDeclaration>();
		List<MethodDeclaration> originalMethods = new ArrayList<MethodDeclaration>();


		//handler the constructor
		for (TypeDeclaration typeDeclaration : compChanged.getTypes()) {
			setClassName(compChanged.getPackage().getName() + Difference.DOT + typeDeclaration.getName());
			List<BodyDeclaration> body = typeDeclaration.getMembers();
			for (BodyDeclaration bodyDeclaration : body) {
				if (bodyDeclaration instanceof FieldDeclaration) {
					changedFields.add(bodyDeclaration);
				}
				else if (bodyDeclaration instanceof ConstructorDeclaration) {
					changedConstructor.add((ConstructorDeclaration) bodyDeclaration);
				}
			}
		}

		for (TypeDeclaration typeDeclaration : compOld.getTypes()) {

			List<BodyDeclaration> body = typeDeclaration.getMembers();

			for (BodyDeclaration bodyDeclaration : body) {
				if (bodyDeclaration instanceof FieldDeclaration) {
					originalFields.add(bodyDeclaration);
				}
				else if (bodyDeclaration instanceof ConstructorDeclaration) {
					originalConstructor.add((ConstructorDeclaration) bodyDeclaration);
				}
			}
		}

		for (TypeDeclaration typeDeclaration : compChanged.getTypes()) {
			setClassName(compChanged.getPackage().getName() +Difference.DOT+typeDeclaration.getName());
			List<BodyDeclaration> body = typeDeclaration.getMembers();
			for (BodyDeclaration bodyDeclaration : body) {
				if (bodyDeclaration instanceof FieldDeclaration) {
					changedFields.add(bodyDeclaration); 
				}
				else if (bodyDeclaration instanceof MethodDeclaration) {
					changedMethods.add((MethodDeclaration) bodyDeclaration);
				}
			}
		}

		for (TypeDeclaration typeDeclaration : compOld.getTypes()) {

			List<BodyDeclaration> body = typeDeclaration.getMembers();

			for (BodyDeclaration bodyDeclaration : body) {
				if (bodyDeclaration instanceof FieldDeclaration) {
					originalFields.add(bodyDeclaration); 
				}
				else if (bodyDeclaration instanceof MethodDeclaration) {
					originalMethods.add((MethodDeclaration) bodyDeclaration);
				}
			}
		}

		// verify the constructor differences
		diffConstructors(changedConstructor, originalConstructor);
		diffMethods(changedMethods, originalMethods);
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
						lines.addAll(getAllLineStatementsMethod(stt, constructor));
					}
					blockDiff.addAll(lines);
				}
			}
		}
		if(!encontrou){
			List<String> lines = new ArrayList<String>();
			for (Statement stt : constructor.getBlock().getStmts()) {
				lines.addAll(getAllLineStatementsMethod(stt, constructor));
			}
			blockDiff.addAll(lines);
		}
	}


	/**
	 * Take the difference between the file methods.
	 * 
	 * @param changedMethods
	 * 			List<MethodDeclaration> with the changed methods.
	 * @param originalMethods
	 * 			List<MethodDeclaration> with the original methods.
	 */
	private void diffMethods(final List<MethodDeclaration> changedMethods, final List<MethodDeclaration> originalMethods) {

		for (MethodDeclaration changedMethod : changedMethods) {
			checkMethods(changedMethod, originalMethods);
			checkStatement(changedMethod, originalMethods);
		}
	}
	
	/**
	 * 
	 * Find the change method signature into the original methods declaration and takes the difference.
	 * 
	 * @param changedMethod
	 * 			<MethodDeclaration> with the method signature to be found into the original methods declaration.
	 * @param originalMethods
	 * 			List<MethodDeclaration> with the original methods declaration.
	 * 
	 * @return The original MethodDeclaration if exists, else return NULL.
	 */
	private MethodDeclaration findMethodInOriginalList(final MethodDeclaration changedMethod, final List<MethodDeclaration> originalMethods) {
		for (MethodDeclaration originalMethod : originalMethods) {
			String changedMethodSignature = generateAssignature(changedMethod);

			String originalMethodSignature = generateAssignature(originalMethod);

			if (changedMethodSignature.equals(originalMethodSignature)) {
				return originalMethod;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param changedMethod
	 * @param originalMethods
	 */
	private void checkStatement(final MethodDeclaration changedMethod, final List<MethodDeclaration> originalMethods) {
		MethodDeclaration originalMethod = findMethodInOriginalList(changedMethod, originalMethods);
		
		if(originalMethod != null) {
			boolean isDiff = isDiffMethods(changedMethod.getBody(), originalMethod.getBody());
			if (isDiff) {
				List<String> changedLines = new ArrayList<String>();
				List<String> originalLines = new ArrayList<String>();
				for (Statement changedStatement : changedMethod.getBody().getStmts()) {
					changedLines.addAll(getAllLineStatementsMethod(changedStatement, changedMethod));
				}
				
				for (Statement originalStatement : originalMethod.getBody().getStmts()) {
					originalLines.addAll(getAllLineStatementsMethod(originalStatement, originalMethod));
				}
				statementDiff.addAll(changedLines);
			}
		} else {
			List<String> lines = new ArrayList<String>();
			for (Statement stt : changedMethod.getBody().getStmts()) {
				lines.addAll(getAllLineStatementsMethod(stt, changedMethod));
			}
			statementDiff.addAll(lines);
		}
	}
	
//	private String deletionAnalysis(final List<String> changedLines, final List<String> originalLines) {
//		// Pesquisar por possíveis inserções.
//		
//		if(changedLines.size() < originalLines.size()) {
//			// If the first lines are different it's because in the new version the first line(s) were deleted.
//			if(!changedLines.get(0).equals(originalLines.get(0))) {
//				// get the next one to represent.
//				return changedLines.get(0);
//			} 
//			// If the last ones are different it's because in the new version the last line(s) were deleted.
//			else if(!changedLines.get(changedLines.size()).equals(
//					originalLines.get(originalLines.size()))) {
//				// get the previous one.
//				return changedLines.get(changedLines.size());
//			}
//			// If because the change occurs in the middle.
//			else {
//				// Find the difference and return the nextOne.
//				return null;
//			}
//		}
//	}
	
	private void checkMethods(final MethodDeclaration changedMethod, final List<MethodDeclaration> originalMethods) {

		boolean found = false;
		for (MethodDeclaration originalMethod : originalMethods) {
			String changedMethodSignature = generateAssignature(changedMethod);

			String originalMethodSignature = generateAssignature(originalMethod);

			if (changedMethodSignature.equals(originalMethodSignature)) {
				found = true;
				boolean isDiff = isDiffMethods(changedMethod.getBody(), originalMethod.getBody());
				if (isDiff) {
					List<String> lines = new ArrayList<String>();
					for (Statement stt : changedMethod.getBody().getStmts()) {
						lines.addAll(getAllLineStatementsMethod(stt, changedMethod));
					}
					blockDiff.addAll(lines);
				}
			}
		}
		if(!found){
			List<String> lines = new ArrayList<String>();
			for (Statement stt : changedMethod.getBody().getStmts()) {
				lines.addAll(getAllLineStatementsMethod(stt, changedMethod));
			}
			blockDiff.addAll(lines);
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

	/**
	 * Format all statements into the PriorJ format work.
	 * 
	 * @param changedStatement
	 * 			Statement changed.
	 * @param methodSignature
	 * 			Method signature.
	 * @return List<String> with all method statements in the string format.
	 */
	private List<String> getAllLineStatementsMethod(final Statement changedStatement, final MethodDeclaration method) {
		List<String> stmts = new ArrayList<String>();

		if (changedStatement == null) {
			return stmts;
		}

		if (changedStatement instanceof BlockStmt) {

			BlockStmt bloco = (BlockStmt) changedStatement;
			
			for (Statement st : bloco.getStmts()) {
				stmts.addAll(getAllLineStatementsMethod(st, method));
			}
		} else if (changedStatement instanceof IfStmt) {

			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			stmts.add(line);

			IfStmt ifSt = (IfStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(ifSt.getThenStmt(),method));

			stmts.addAll(getAllLineStatementsMethod(ifSt.getElseStmt(), method));
		} else if (changedStatement instanceof ForStmt) {

			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			stmts.add(line);

			ForStmt forSt = (ForStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), method));
		} else if (changedStatement instanceof ForeachStmt) {

			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			stmts.add(line);

			ForeachStmt forSt = (ForeachStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), method));
		} else if (changedStatement instanceof WhileStmt) {

			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			stmts.add(line);

			WhileStmt forSt = (WhileStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), method));
		} else if (changedStatement instanceof TryStmt) {
			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			stmts.add(line);

			TryStmt trySt = (TryStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(trySt.getTryBlock(), method));

			stmts.addAll(getAllLineStatementsMethod(trySt.getFinallyBlock(), method));
		} else {

			String line = getClassName() + Difference.DOT + method.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(method);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			System.out.println(line);
			if (!changedStatement.toString().contains("watchPrior")) {
				stmts.add(line);
			}
		}

		return stmts;
	}
	
	/**
	 * Format all statements into the PriorJ format work.
	 * 
	 * @param changedStatement
	 * 			Statement changed.
	 * @param methodSignature
	 * 			Method signature.
	 * @return List<String> with all method statements in the string format.
	 */
	private List<String> getAllLineStatementsMethod(final Statement changedStatement, final ConstructorDeclaration constructor) {
		List<String> stmts = new ArrayList<String>();

		if (changedStatement == null) {
			return stmts;
		}

		if (changedStatement instanceof BlockStmt) {

			BlockStmt bloco = (BlockStmt) changedStatement;
			
			for (Statement st : bloco.getStmts()) {
				stmts.addAll(getAllLineStatementsMethod(st, constructor));
			}
		} else if (changedStatement instanceof IfStmt) {

			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			
			stmts.add(line);

			IfStmt ifSt = (IfStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(ifSt.getThenStmt(),constructor));

			stmts.addAll(getAllLineStatementsMethod(ifSt.getElseStmt(), constructor));
		} else if (changedStatement instanceof ForStmt) {

			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			stmts.add(line);

			ForStmt forSt = (ForStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), constructor));
		} else if (changedStatement instanceof ForeachStmt) {

			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			stmts.add(line);

			ForeachStmt forSt = (ForeachStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), constructor));
		} else if (changedStatement instanceof WhileStmt) {

			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			stmts.add(line);

			WhileStmt forSt = (WhileStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(forSt.getBody(), constructor));
		} else if (changedStatement instanceof TryStmt) {
			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();
			stmts.add(line);

			TryStmt trySt = (TryStmt) changedStatement;

			stmts.addAll(getAllLineStatementsMethod(trySt.getTryBlock(), constructor));

			stmts.addAll(getAllLineStatementsMethod(trySt.getFinallyBlock(), constructor));
		} else {

			String line = getClassName() + Difference.DOT + constructor.getName() + Difference.PARENTHESES_OPEN;
			line += extractParameters(constructor);
			line += Difference.PARENTHESES_CLOSED + Difference.DOT + changedStatement.getBeginLine();

			if (!changedStatement.toString().contains("watchPrior")) {
				stmts.add(line);
			}
		}

		return stmts;
	}
	
	/**
	 * Extract the parameters from MethodDeclaration.
	 * 
	 * @param method 
	 * 			MethodDeclaration to be extracted.
	 * @return String with the parameters.
	 */
	private String extractParameters(final MethodDeclaration method) {
		String extract = "";
		List<Parameter> parameters = method.getParameters();
		if(parameters == null) {
			return "";
		}
		for (Parameter parameter : parameters) {
			extract += parameter.getType() + Difference.COMMA;
		}
		// Excluindo a última vírgula.
		if(extract.endsWith(Difference.COMMA)) {
			extract = extract.substring(0, extract.length()-1);
		}
		return extract;
	}
	
	/**
	 * Extract the parameters from MethodDeclaration.
	 * 
	 * @param constructor 
	 * 			MethodDeclaration to be extracted.
	 * @return String with the parameters.
	 */
	private String extractParameters(final ConstructorDeclaration constructor) {
		String extract = "";
		List<Parameter> parameters = constructor.getParameters();
		for (Parameter parameter : parameters) {
			extract += parameter.getType() + Difference.COMMA;
		}
		// Excluindo a última vírgula.
		if(extract.endsWith(Difference.COMMA)) {
			extract = extract.substring(0, extract.length()-1);
		}
		return extract;
	}
	
	/**
	 * Analyze if the constructor are different.
	 * 
	 * @param changedBlock
	 * 			BlockStmt with the changed block.
	 * @param originalBlock
	 * 			BlockStmt with the old block.
	 * 
	 * @return true if it's different, else return false.
	 */
	private boolean isDiffConstructors(final BlockStmt changedBlock, final BlockStmt originalBlock) {
		return isDiffMethods(changedBlock, originalBlock);
	}

	/**
	 * Analyze if the methods are different.
	 * 
	 * @param changedBlock
	 * 			BlockStmt with the changed block.
	 * @param originalBlock
	 * 			BlockStmt with the old block.
	 * 
	 * @return true if it's different, else return false.
	 */
	private boolean isDiffMethods(final BlockStmt changedBlock, final BlockStmt originalBlock) {

		List<Statement> stChanged = null;

		List<Statement> stOriginal = null;

		if (changedBlock != null) {
			stChanged = changedBlock.getStmts();
		}

		if (originalBlock != null) {
			stOriginal = originalBlock.getStmts();
		}

		if (stChanged == stOriginal) {
			return false;
		}

		// Changed or Original are null.
		if (stChanged == null && stOriginal != null) {
			return true;
		}

		if (stChanged != null && stOriginal == null) {
			return true;
		}

		// Different size.
		if (stChanged.size() != stOriginal.size()) {
			return true;
		}

		// Any statement is different.
		for (int i = 0; i < stChanged.size(); i++) {
			if (!isEqualsStatement(stChanged.get(i), stOriginal.get(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Analyze if the statements are different.
	 * 
	 * @param changedStatement
	 * 			Statement changed.
	 * @param originalStatement
	 * 			Statement original.
	 * @return true if it's equals, else return false;
	 */
	private boolean isEqualsStatement(final Statement changedStatement, final Statement originalStatement) {
		return changedStatement.toString().equals(originalStatement.toString());
	}

}

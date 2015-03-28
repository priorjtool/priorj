import org.aspectj.lang.Signature;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.edu.ufcg.splab.coverage.dao.Repository;
import com.edu.ufcg.splab.coverage.annotations.*;


/**
 * This class is a aspect class responsible by coverage trace.
 * 
 * @author Samuel T. C. Santos
 * @version 1.0 
 *
 */
public aspect AspectCoverage {
	private Repository repository;
//	private Logger logger = Logger.getLogger("trace");
	pointcut traceMethods() : execution(@MethodDeclaration * *(..)) && !within(AspectCoverage);
	pointcut traceJUnitTests () : execution(@TestCaseDeclaration * *(..)) && !within(AspectCoverage);
	pointcut traceStatements () : get(* *.watchPriorJApp);
	pointcut afterRunSuite () : execution(@AfterClass * *(..)); 
	pointcut beforeRunSuite () : execution(@BeforeClass * *(..)); 
	
	after(): afterRunSuite(){
		if(repository != null){
			repository.commit();
			repository.finishCommit();
		}
	}
	
	before(): beforeRunSuite(){
		repository = new Repository();
		repository.init();
	}
	
	before () : traceJUnitTests() {
		Signature sig = thisJoinPointStaticPart.getSignature();
        String pkgName = sig.getDeclaringType().getPackage().getName();
        String suiteName = thisJoinPoint.getTarget().getClass().getSimpleName();
        String tcName = sig.getName();
        repository.addTestCase(pkgName, suiteName, tcName);
		//logger.logp(Level.INFO, sig.getDeclaringType().getName(), sig.getName(), "Test Case");
	}
//	after(): traceJUnitTests(){
//		if(repository != null) repository.commit();
//	}
	before () : traceMethods(){
		Signature sig = thisJoinPointStaticPart.getSignature();
		Class pck = sig.getDeclaringType();
		String pckName = "";
		if (pck.getPackage() != null) pckName = sig.getDeclaringType().getPackage().getName();
		String classeName = sig.getDeclaringType().getName();
		classeName = classeName.replace(pckName+".", "");	
		String name = sig.toString();
		int index =  name.lastIndexOf(".")+1;
		name = name.substring(index, name.length());
		if(repository != null) repository.addMethod(pckName, classeName, name);
//		logger.logp(Level.INFO, sig.getDeclaringType().getName(), sig.getName(), "Method");
	}
	before () : traceStatements(){
		int lineNumber = thisJoinPointStaticPart.getSourceLocation().getLine() + 1;
		if(repository != null)repository.addStatement(lineNumber + "");
//		logger.logp(Level.INFO, "", String.valueOf(lineNumber), "Line");
	}
}

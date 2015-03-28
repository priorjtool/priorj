package com.edu.ufcg.splab.coverage.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a marker to Test Case Declaration 
 * 
 * @author Samuel T. C. Santos
 *
 */
@Target(value= ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestCaseDeclaration {

}

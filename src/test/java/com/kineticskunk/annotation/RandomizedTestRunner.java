package com.kineticskunk.annotation;

import java.lang.reflect.Method;

import annotation.Randomized;
import annotation.Randomized.randomType;

public class RandomizedTestRunner {
	
	public static void main(String[] args) {

		RandomizedAnnotationTest runner = new RandomizedAnnotationTest();
        Method[] methods = runner.getClass().getMethods();

        for (Method method : methods) {
        	Randomized annos = method.getAnnotation(Randomized.class);
            if (annos != null) {
                try {
                	if (annos.randomAlphaNumeric().equals(randomType.ALPHANUMERIC)) {
                		method.invoke(runner, annos.length());
                	}
                    
                	if (annos.randomAlphaNumeric().equals(randomType.NUMERIC)) {
                		method.invoke(runner, annos.min(), annos.max());
                	}
                	
                	if (annos.randomAlphaNumeric().equals(randomType.NUMERIC)) {
                		method.invoke(runner, annos.min(), annos.max());
                	}
                	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

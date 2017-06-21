package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD } ) //can use in method only.

public @interface Randomized {

	public enum randomType {
		ALPHANUMERIC,
		NUMERIC,
		ALPHA
	}

	randomType randomAlphaNumeric() default randomType.ALPHANUMERIC;

	int length() default 1;

	int min() default 1;

	int max() default 1;
	
	String randomValue()  default "xxxxxx";

}
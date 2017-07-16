package com.kineticskunk.annotation;

import com.kineticskunk.annotation.Randomized;
import com.kineticskunk.annotation.Randomized.randomType;
import com.kineticskunk.library.RandomValues;

public class RandomizedAnnotationTest {
	
	private RandomValues rv = new RandomValues();
	
	@Randomized(length = 10, randomAlphaNumeric = randomType.ALPHANUMERIC)
	public void TestRandomAlphaNumeric(int length) {
		System.out.println(rv.randomAlphaNumeric(length));
	}
	
	@Randomized(min = 10, max = 20, randomAlphaNumeric = randomType.NUMERIC)
	public void TestRandomAlpha(int min, int max) {
		System.out.println(rv.randomNumeric(min, max));
	}
	
	@Randomized(length = 10, randomAlphaNumeric = randomType.ALPHA)
	public void TestRandomNumeric(int length) {
		System.out.println(rv.randomAlpha(length));
	}
	
}

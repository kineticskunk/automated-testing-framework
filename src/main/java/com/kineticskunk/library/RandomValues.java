package com.kineticskunk.library;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class RandomValues {
	
	public String randomAlphaNumeric(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = length; i > 0; i -= 12) {
			int n = Math.min(12, Math.abs(i));
			sb.append(StringUtils.leftPad(Long.toString(Math.round(Math.random() * Math.pow(36.0, n)), 36), n, '0'));
		}
		return sb.toString();
	}
	
	public int randomNumeric(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}
	
	public String randomAlpha(int length) {
		int leftLimit = 97;
		int rightLimit = 122;
		String randomValue = null;
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; ++i) {
			int randomLimitedInt = leftLimit + (int)(new Random().nextFloat() * (float)(rightLimit - leftLimit));
			buffer.append((char)randomLimitedInt);
		}
		randomValue = buffer.toString();
		return randomValue;
	}

}

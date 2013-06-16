package net.pechorina.kontempl.utils;

import org.apache.commons.lang.RandomStringUtils;

public class PasswordUtils {
	
	public static String getAlphaNum(int size) {
		return RandomStringUtils.randomAlphanumeric(size);
	}
	
	public static String getAlpha(int size) {
		return RandomStringUtils.randomAlphabetic(size);
	}
}

package net.pechorina.kontempl.utils.format;

import java.text.ParseException;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.format.Formatter;

public final class LowerCaseTrimFormatter implements Formatter<String> {
	private static final Logger logger = Logger.getLogger(LowerCaseTrimFormatter.class);
	@Override
	public String print(String s, Locale locale) {
		logger.debug("print, in=[" + s + "]");
		return s.toLowerCase().trim();
	}

	@Override
	public String parse(String text, Locale locale) throws ParseException {
		logger.debug("parse, in=[" + text + "]");
		if (text == null) {
			return null;
		}
		return text.toLowerCase().trim();
	}

}

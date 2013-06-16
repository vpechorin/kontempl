package net.pechorina.kontempl.utils.format;

import java.util.HashSet;
import java.util.Set;

import net.pechorina.kontempl.utils.format.annotation.LowerCaseTrimFormat;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class LowerCaseTrimFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LowerCaseTrimFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> h = new HashSet<Class<?>>();
		h.add(String.class);
		return h;
	}

	@Override
	public Printer<?> getPrinter(LowerCaseTrimFormat annotation, Class<?> fieldType) {
		return new LowerCaseTrimFormatter();
	}

	@Override
	public Parser<?> getParser(LowerCaseTrimFormat annotation, Class<?> fieldType) {
		return new LowerCaseTrimFormatter();
	}

}

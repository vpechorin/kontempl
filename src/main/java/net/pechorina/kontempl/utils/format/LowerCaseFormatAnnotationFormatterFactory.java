package net.pechorina.kontempl.utils.format;

import java.util.HashSet;
import java.util.Set;

import net.pechorina.kontempl.utils.format.annotation.LowerCaseFormat;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class LowerCaseFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<LowerCaseFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> h = new HashSet<Class<?>>();
		h.add(String.class);
		return h;
	}

	@Override
	public Printer<?> getPrinter(LowerCaseFormat annotation, Class<?> fieldType) {
		return new LowerCaseFormatter();
	}

	@Override
	public Parser<?> getParser(LowerCaseFormat annotation, Class<?> fieldType) {
		return new LowerCaseFormatter();
	}

}

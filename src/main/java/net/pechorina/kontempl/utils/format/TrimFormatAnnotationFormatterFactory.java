package net.pechorina.kontempl.utils.format;

import java.util.HashSet;
import java.util.Set;

import net.pechorina.kontempl.utils.format.annotation.TrimFormat;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

public class TrimFormatAnnotationFormatterFactory implements AnnotationFormatterFactory<TrimFormat> {

	@Override
	public Set<Class<?>> getFieldTypes() {
		Set<Class<?>> h = new HashSet<Class<?>>();
		h.add(String.class);
		return h;
	}

	@Override
	public Printer<?> getPrinter(TrimFormat annotation, Class<?> fieldType) {
		return new TrimFormatter();
	}

	@Override
	public Parser<?> getParser(TrimFormat annotation, Class<?> fieldType) {
		return new TrimFormatter();
	}

}

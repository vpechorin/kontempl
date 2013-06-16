package net.pechorina.kontempl.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {
	
	private DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String marshal(DateTime v) throws Exception {
    	if (v == null) return null;
        return v.toString(fmt);
    }

    @Override
    public DateTime unmarshal(String v) throws Exception {
    	if (v == null) return null;
    	return fmt.parseDateTime(v);
    }

}

package net.pechorina.kontempl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat(" yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String marshal(Date v) throws Exception {
    	if (v == null) return null;
        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	if (v == null) return null;
        return dateFormat.parse(v);
    }

}

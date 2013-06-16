package net.pechorina.kontempl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<String, Timestamp> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public String marshal(Timestamp v) throws Exception {
    	if (v == null) return null;
        return dateFormat.format(v);
    }

    @Override
    public Timestamp unmarshal(String v) throws Exception {
    	if (v == null) return null;
    	Date d = dateFormat.parse(v);
    	Timestamp t = new Timestamp(d.getTime());
        return t;
    }

}

package net.pechorina.kontempl.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public String marshal(BigDecimal v) throws Exception {
    	if (v == null) return null;
    	v.setScale(3, RoundingMode.HALF_UP);
        return v.toPlainString();
    }

    @Override
    public BigDecimal unmarshal(String v) throws Exception {
    	if (v == null) return null;
    	return new BigDecimal(v);
    }

}

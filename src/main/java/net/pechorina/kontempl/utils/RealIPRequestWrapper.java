package net.pechorina.kontempl.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RealIPRequestWrapper extends HttpServletRequestWrapper {
	public RealIPRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public String getRemoteAddr() {
		String realIP = super.getHeader("X-Real-IP");
		return realIP!=null?realIP:super.getRemoteAddr();
	}
	
	@Override
	public String getRemoteHost() {
		try {
			return InetAddress.getByName(getRemoteAddr()).getHostName();
		} catch (UnknownHostException e) {
			return getRemoteAddr();
		}
	}
}

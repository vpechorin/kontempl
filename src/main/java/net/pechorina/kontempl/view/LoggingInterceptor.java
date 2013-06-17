package net.pechorina.kontempl.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.service.ProfilingService;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component("loggingInterceptor")
public class LoggingInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(LoggingInterceptor.class);
	
	private static final DateTimeFormatter fmtW3C = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
	
    @Autowired
    @Qualifier("appConfig")
    public java.util.Properties appConfig;

    @Autowired
    public ProfilingService profilingService;   
	
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
		Integer h = request.hashCode();
		request.setAttribute("requestId", h);
		profilingService.onRequestStart("Request #" + h + " started");
		logger.debug("user principal: " + request.getUserPrincipal());
        return true;
    }
	
	public void postHandle (HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler, 
			ModelAndView modelAndView) throws Exception {
		
		if (request.getAttribute("last-modified") != null) {
			String lastModified = (String) request.getAttribute("last-modified");
			logger.debug("lastModified:" + lastModified);
			response.addHeader("last-modified", lastModified);
		}
		DateTime d = new DateTime();
		DateTime dtUTC = d.withZone(DateTimeZone.UTC);
		
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "private");
		response.addHeader("Expires", dtUTC.toString(fmtW3C));
		
		response.addHeader("X-App", appConfig.getProperty("application.name") +"-" + appConfig.getProperty("application.build"));
		response.addHeader("X-Dev", appConfig.getProperty("application.developer") + " " + appConfig.getProperty("application.developerEmail"));
	}
	
	public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex)
            throws Exception {
		Integer h = (Integer) request.getAttribute("requestId");
		profilingService.logElapsedTime("request #" + h + " processing completed - ");
		if (response != null) {
			logger.debug("Response: " + response.getStatus());
		}
		if (ex != null) {
			logger.error("Exception: " + ex);
		}
	}
}

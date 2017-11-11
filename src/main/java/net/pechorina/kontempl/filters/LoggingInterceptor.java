package net.pechorina.kontempl.filters;

import net.pechorina.kontempl.service.ProfilingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("loggingInterceptor")
public class LoggingInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
	
    @Autowired
    private Environment env;

    @Autowired
    private ProfilingService profilingService;   
	
	public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
			Integer h = request.hashCode();
			profilingService.onRequestStart("Request [" + h + "] - started");			
			request.setAttribute("requestId", h);

			logger.debug("user principal: " + request.getUserPrincipal());
        return true;
    }
	
	public void postHandle (HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler, 
			ModelAndView modelAndView) throws Exception {
	}
	
	public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex)
            throws Exception {
		Integer h = (Integer) request.getAttribute("requestId");
		profilingService.logElapsedTime("Request [" + h + "] - processing completed - ");
		if (response != null) {
			logger.debug("Response: " + response.getStatus());
		}
		if (ex != null) {
			logger.error("Exception: " + ex);
		}
	}
}

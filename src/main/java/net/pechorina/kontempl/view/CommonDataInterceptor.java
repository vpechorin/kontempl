package net.pechorina.kontempl.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.ProfilingService;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.utils.TextContentUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//@Component
public class CommonDataInterceptor extends HandlerInterceptorAdapter  {
	static final Logger logger = LoggerFactory.getLogger(CommonDataInterceptor.class);
	
    @Autowired
    private Environment env;
    
    @Autowired
    private ProfilingService profilingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private PageTreeService pageTreeService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void afterCompletion(HttpServletRequest request,
    HttpServletResponse response,
    Object handler,
    Exception ex)
			throws Exception {
		
	}

	/*@Override
	public void postHandle(WebRequest webRequest, ModelMap model) throws Exception {
		logger.debug("CommonDataInterceptor::postHandle");
		if (model != null) {
			
			model.addAttribute("appVersion", env.getProperty("application.build"));
			model.addAttribute("env", env);
			
			User user = this.getUser();
			if (user != null) {
				model.addAttribute("user", user);
				logger.debug("added user to the model");
			}
			
			TextContentUtils tc = new TextContentUtils();
			model.addAttribute("textProcessor", tc);

		}
		else {
			logger.debug("model is NULL");
		}

	}

	@Override
	public void preHandle(WebRequest webRequest) throws Exception {
	}
    
    private User getUser() {
    	User user = null;
    	Authentication a = SecurityContextHolder.getContext().getAuthentication();
    	if (a != null) {
    		Object principal = a.getPrincipal();
    		if (principal != null) {
    			if (principal instanceof UserDetails) {
    				UserDetails userDetails = (UserDetails) principal;
    				OptiUserDetails s = (OptiUserDetails)userDetails;
    				user = s.getUser();
    			} 
    		}
    	}
    	
    	return user;
    }*/
}

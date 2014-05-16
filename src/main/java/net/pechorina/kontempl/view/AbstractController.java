package net.pechorina.kontempl.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.PageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
abstract public class AbstractController {
    
    @Autowired
    @Qualifier("appConfig")
    public java.util.Properties appConfig;
    
	@Autowired
    public PageService pageService;
	
	@Autowired
	public Authentication auth;
	
	@Autowired
	public HttpSession session;
	
	@Autowired
	public HttpServletRequest request;
	
	public User getUser(){
		User user = null;
		if (auth != null && auth.isAuthenticated()) {
	        Object principal = auth.getPrincipal();
	        if (principal instanceof UserDetails) {
	        	UserDetails userDetails = (UserDetails) principal;
	        	OptiUserDetails s = (OptiUserDetails)userDetails;
	        	user = s.getUser();
	        }
	    }
	    return user;
	}

	public OptiUserDetails getUserDetails() {
		OptiUserDetails s = null;
		if (auth != null && auth.isAuthenticated()) {
	        Object principal = auth.getPrincipal();
	        if (principal instanceof UserDetails) {
	        	UserDetails userDetails = (UserDetails) principal;
	        	s = (OptiUserDetails)userDetails;
	        }
	    }		
		return s;
	}

	
	public Authentication getAuth() {
		return auth;
	}
}

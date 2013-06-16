package net.pechorina.kontempl.utils;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationHolder {
	private static final Logger logger = Logger.getLogger(AuthenticationHolder.class);
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public User getUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = null;
		if (auth != null && auth.isAuthenticated()) {
	        Object principal = auth.getPrincipal();
	        logger.debug("principal: " + principal);
	        if (principal instanceof UserDetails) {
	        	UserDetails userDetails = (UserDetails) principal;
	        	logger.debug("userDetails: " + userDetails);
	        	OptiUserDetails s = (OptiUserDetails)userDetails;
	        	user = s.getUser();
	        	logger.debug("user is: " + user);
	        }
	        else {
	        	logger.debug("Found anonymous user: " + auth.getPrincipal());
	        }
	    }
	    return user;
	}
}

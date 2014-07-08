package net.pechorina.kontempl.rest;

import javax.servlet.http.HttpServletRequest;

import net.pechorina.kontempl.data.AuthToken;
import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.TokenTransfer;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.exceptions.UnauthorizedException;
import net.pechorina.kontempl.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserResource {
	static final Logger logger = LoggerFactory.getLogger(UserResource.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authManager;
	
	@RequestMapping(method = RequestMethod.GET) 
	public User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		User u = null;

		if (authentication != null && authentication.isAuthenticated()) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof String && ((String) principal).equals("anonymousUser")) {
				logger.debug("Found unauthenticated request");
				throw new UnauthorizedException();	
			}
			
			if (principal instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) principal;
	        	OptiUserDetails s = (OptiUserDetails)userDetails;
	        	u = s.getUser();
				logger.debug("Found authenticated user: " + u);
			}
		}
		
		return u;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/authenticate")
	public TokenTransfer authenicate(@RequestParam(value="username", required=true) String username, 
			@RequestParam(value="password", required=true) String password,
			HttpServletRequest request) {
		logger.debug("username/password: " + username + "/" + password);
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);

		Authentication authentication = this.authManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String ip = request.getRemoteAddr();
		String ua = request.getHeader("User-Agent");
		User u = getUserForAuth(authentication); 
		AuthToken authToken = userService.createNewAuthToken(u, ip, ua);
		
		logger.debug("New authToken: " + authToken);
		logger.debug("AUTH OK");

		return new TokenTransfer( authToken.getUuid() );
	}
	
	private User getUserForAuth(Authentication a){
		User user = null;
		if (a != null && a.isAuthenticated()) {
	        Object principal = a.getPrincipal();
	        if (principal instanceof UserDetails) {
	        	UserDetails userDetails = (UserDetails) principal;
	        	OptiUserDetails s = (OptiUserDetails)userDetails;
	        	user = s.getUser();
	        }
	    }
	    return user;
	}
}

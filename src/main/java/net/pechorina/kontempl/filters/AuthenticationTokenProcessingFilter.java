package net.pechorina.kontempl.filters;

import net.pechorina.kontempl.data.AuthToken;
import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("authenticationTokenProcessingFilter")
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);
	
	@Autowired
	private UserService userService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = this.getAsHttpRequest(request);

		String token = this.extractAuthTokenIdFromRequest(httpRequest);
		logger.debug("Token found: " + token);
		if (token != null) {
			AuthToken authToken = userService.getCurrentAuthToken(token);
			
			if (authToken != null) {
				User user = authToken.getUser();
				logger.debug("User retrieved: " + user);
		       	
		       	UserDetails userDetails = new OptiUserDetails(user, user.getRoles());
		       	UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		       	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
		       	SecurityContextHolder.getContext().setAuthentication(authentication);
		       	logger.debug("security context updated with user details");
			}
		}

		chain.doFilter(request, response);
	}
	
	private String extractAuthTokenIdFromRequest(HttpServletRequest httpRequest) {
		/* Get token from header */
		String authToken = httpRequest.getHeader("X-Auth-Token");

		/* If token not found get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}

		return authToken;
	}
	
	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}
}

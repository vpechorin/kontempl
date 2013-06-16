package net.pechorina.kontempl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.pechorina.kontempl.utils.StringUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

public class LocalAuthProvider implements AuthenticationProvider {

	private static final Logger logger = Logger.getLogger(LocalAuthProvider.class);
	protected boolean hideUserNotFoundExceptions = false;
	// protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	@Autowired
	@Qualifier("appConfig")
	private java.util.Properties appConfig;
	
	@Autowired
	@Qualifier("customUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	@Qualifier("securityMessageSource")
	MessageSource messages;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
	            messages.getMessage("UsernamePasswordAuthenticationToken.onlySupports", null,
	                "Only UsernamePasswordAuthenticationToken is supported", Locale.getDefault()));
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());
		
		// clean username and password
		username = username.toLowerCase();
		username = StringUtils.superTrim(username);
		
		password = StringUtils.superTrim(password);
		
		logger.debug("username: `" + username + "`");
		logger.debug("password: `" + password + "`");

		// 1. Use the username to load the data for the user, including
		// authorities and password.
		
		UserDetails ud;
        try {
        	ud = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException notFound) {
            if (hideUserNotFoundExceptions) {
            	logger.warn("username not found: " + username);
                throw new BadCredentialsException(messages.getMessage(
                        "LocalAuthProvider.badCredentials", null,  "Bad credentials", Locale.getDefault()));
            } else {
                throw notFound;
            }
        }

		// 2. Check the passwords match.
        
		if (!ud.getPassword().equals(DigestUtils.shaHex(password))) {
			throw new BadCredentialsException(messages.getMessage(
                    "LocalAuthProvider.incorrectPassword", null, "Bad password", Locale.getDefault()));
		}

		// 3. Return an authenticated token, containing user data and
		// authorities
		UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
		return t;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities(List<String> aasRoles) {
		List<GrantedAuthority> temp = new ArrayList<GrantedAuthority>();
		for (String roleName : aasRoles) {
			temp.add( new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()) );
		}
		return Collections.unmodifiableList(temp);
	}
	
	@Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
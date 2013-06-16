package net.pechorina.kontempl.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.Role;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserDetailsService.class);
	//protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	@Autowired
	@Qualifier("appConfig")
	public java.util.Properties appConfig;
	
	@Autowired
	UserService userService;
	
	@Autowired
	@Qualifier("securityMessageSource")
	MessageSource messages;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = null;
		if (username != null) {
			username = StringUtils.superTrim(username);
		}

		user = userService.getUserByEmail(username);
		
       	if (user == null) {
       		throw new UsernameNotFoundException(messages.getMessage("LocalAuthProvider.userNotFound", null, Locale.getDefault()));
       	}
       	
       	Set<String> roles = new HashSet<String>();
       	if (user.getRoles() != null) {
       		for(Role r: user.getRoles()) {
       			roles.add(r.getName());
       		}
       	}
       	
       	UserDetails ud = new OptiUserDetails(user, roles);
       	
		return ud;
	}
	
}

package net.pechorina.kontempl.service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.utils.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);
	//protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	
	@Autowired
	private Environment env;
	
	@Autowired
	UserService userService;
	
	@Autowired
	//@Qualifier("securityMessageSource")
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
       		roles = user.getRoles();
       	}
       	
       	UserDetails ud = new OptiUserDetails(user, roles);
       	
		return ud;
	}
	
}

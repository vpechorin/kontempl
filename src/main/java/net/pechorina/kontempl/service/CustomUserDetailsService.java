package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	UserService userService;
	
	@Autowired
	//@Qualifier("securityMessageSource")
	MessageSource messages;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		if (username != null) {
			username = StringUtils.superTrim(username);
		}

		User user = userService.getUserByEmail(username);
		
       	if (user == null) {
       		throw new UsernameNotFoundException(messages.getMessage("LocalAuthProvider.userNotFound", null, Locale.getDefault()));
       	}
       	
       	Set<String> roles = new HashSet<>();
       	if (user.getRoles() != null) {
       		roles = user.getRoles();
       	}

        return new OptiUserDetails(user, roles);
	}
	
}

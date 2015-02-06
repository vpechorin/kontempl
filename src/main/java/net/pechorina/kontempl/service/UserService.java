package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.AuthToken;
import net.pechorina.kontempl.data.Credential;
import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.repos.AuthTokenRepo;
import net.pechorina.kontempl.repos.CredentialRepo;
import net.pechorina.kontempl.repos.UserRepo;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
	static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CredentialRepo credentialRepo;
	
	@Autowired
	private AuthTokenRepo authTokenRepo;

	@Autowired
	private Environment env;
	
	@Transactional
	public AuthToken getCurrentAuthToken(String uuid) {
		
		int tokenExpireMinutes = Integer.parseInt( env.getProperty("auth_token_expire") );
		
		AuthToken a = authTokenRepo.findOne(uuid);
		logger.debug("Token retrieved: " + a);
		// check if it is not expired
		if ( (a != null) && (a.getUpdated().plusMinutes(tokenExpireMinutes).isBeforeNow()) ) {
			// expired
			authTokenRepo.delete(uuid);
			logger.debug("Token expired: " + a);
			return null;
		}
		
		if (a != null) {
			// update token
			a.setUpdated(new DateTime());
			return authTokenRepo.saveAndFlush(a);
		}
		
		return null;
	}
	
	// run every two hours
	@Scheduled(fixedRate=7200000)
	@Transactional
	public void removeExpiredTokens() {
		List<AuthToken> tokens = authTokenRepo.findAll();
		int tokenExpireMinutes = Integer.parseInt( env.getProperty("auth_token_expire") );
		List<AuthToken> expiredTokens = tokens.stream().filter(t -> t.getUpdated().plusMinutes(tokenExpireMinutes).isBeforeNow() ).collect(Collectors.toList());
		int cnt = 0;
		for(AuthToken t: expiredTokens) {
			authTokenRepo.delete(t);
			cnt++;
		}
		if (cnt > 0) {
			logger.info("Removed " + cnt + " expired auth tokens.");
		}
	}
	
	@Transactional
	public AuthToken createNewAuthToken(User user, String ipAddress, String userAgent) {
		AuthToken a = new AuthToken(user, ipAddress, userAgent);
        return authTokenRepo.saveAndFlush(a);
	}
	
	@Transactional
	public User saveNewUser(User u, String email, String password) {
        
        if (( email != null) && (password != null)) {
        	String pwdHash = DigestUtils.sha1Hex(password);
        	String uid = "password:" + email;
        	Credential c = new Credential(u, "password", uid, email, pwdHash);
    		c.setVerified(true);
    		u.addCredential(c);
        }

        return userRepo.saveAndFlush(u);
	}

	@Transactional
	public User getUserById(int id) {
		return userRepo.findOne(id);
	}
	
	@Transactional
	public User getUserByIdDetailed(int id) {
		User u = userRepo.findOne(id);
		retrieveDetails(u);
		return u;
	}
	
	private void retrieveDetails(User u) {
		if (u.getCredentials() != null) u.getCredentials().size();
		if (u.getAuthTokens() != null) u.getAuthTokens().size();
		if (u.getRoles() != null) u.getRoles().size();
	}
	
	@Transactional
	public User getUserByEmail(String email) {
		String uid = "password:" + email;
		Credential c = credentialRepo.findByUid(uid);
		if (c != null) {
			User u = c.getUser();
			int id = u.getId();
			logger.debug("found user: " + id + " " + u.getName());
			return u;
		}
		else {
			return null;
		}
	}
	
	@Transactional
	public Credential getUserCredentialById(int id) {
		return credentialRepo.findOne(id);
	}

	@Transactional
	public List<User> findUsers() {
		return userRepo.listActiveUsers();
	}
	
	@Transactional
	public Set<Credential> getUserCredentials(Integer userId) {
		User u = userRepo.findOne(userId);
		int s = 0;
		if (u.getCredentials() != null) {
			s = u.getCredentials().size();
		}
		logger.debug("total credentials found: " + s);
		return u.getCredentials();
	}
	
	@Transactional
	public Credential getUserCredential(Integer userId, String authType) {
		User u = userRepo.findOne(userId);
        return u.getCredentials().stream().filter(cr -> cr.getAuthServiceType().equalsIgnoreCase(authType)).findFirst().orElse(null);
	}
	
	@Transactional
	public Credential getCredential(Integer id) {
		return credentialRepo.findOne(id);
	}

	@Transactional
	public User save(User user) {
		return userRepo.saveAndFlush(user);
	}
	
	@Transactional
	public void delete(User user) {
		userRepo.delete(user);
	}
	
	@Transactional
	public void saveCredential(Credential entity) {
		credentialRepo.saveAndFlush(entity);
	}
	
	@Transactional
	public void deleteCredential(Credential entity) {
		credentialRepo.delete(entity);
	}
	
	@Transactional
	public List<AuthToken> listAuthTokens(User u) {
		return authTokenRepo.findByUser(u);
	}
	
	@Transactional
	public void deleteAuthToken(AuthToken token) {
		authTokenRepo.delete(token);
	}
	
	@Transactional
	public AuthToken getAuthToken(String uuid) {
		return authTokenRepo.findOne(uuid);
	}
	
	@Transactional
	public void updatePasswordEmailCredential(Credential c, String email, String password) {
    	String pwdHash = DigestUtils.sha1Hex(password);
    	String uid = "password:" + email;
    	c.setEmail(email);
    	c.setUid(uid);
    	c.setAuthData(pwdHash);
    	credentialRepo.saveAndFlush(c);
	}
	
	@Transactional
	public boolean checkIfEmailAvailable(String email) {
		String uid = "password:" + email;
		Credential c = credentialRepo.findByUid(uid);
        return c == null;
    }
	
	public void setUserSession(HttpServletRequest request, User user) {

       	UserDetails ud = new OptiUserDetails(user, user.getRoles());
       	
       	UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(t);
        
        // Create a new session and add the security context.
        HttpSession session = request.getSession(true);
        synchronized (session.getId()) {
        	session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        }
	}
	
}

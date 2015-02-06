package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.AuthToken;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping(value = "/api/users/{userId}/authtokens")
public class AuthTokensResource {
	static final Logger logger = LoggerFactory.getLogger(AuthTokensResource.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET) 
	public List<AuthToken> getTokens(@PathVariable("userId") Integer userId) {
		User u = userService.getUserByIdDetailed(userId);
		return userService.listAuthTokens(u);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{uuid}") 
	public AuthToken getAuthToken(@PathVariable("userId") Integer userId, @PathVariable("uuid") String uuid) {
        return userService.getAuthToken(uuid);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{uuid}")
	public void remove(@PathVariable("userId") Integer userId, 
			@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		AuthToken t = userService.getAuthToken(uuid);
		userService.deleteAuthToken(t);
		logger.info("AUTHTOKEN DELETED: " + uuid + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}

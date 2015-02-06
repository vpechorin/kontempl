package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.Credential;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.view.forms.CredentialPasswordForm;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/users/{userId}/credentials")
public class UserCredentialsResource {
	static final Logger logger = LoggerFactory.getLogger(UserCredentialsResource.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method = RequestMethod.GET) 
	public Set<Credential> getCredentials(@PathVariable("userId") Integer userId) {
		User u = userService.getUserByIdDetailed(userId);
        return u.getCredentials();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}") 
	public Credential getCredential(@PathVariable("userId") Integer userId, @PathVariable("id") Integer id) {
        return userService.getCredential(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void addCredential(@PathVariable("userId") Integer userId, 
			@RequestBody Credential cr, HttpServletRequest request, HttpServletResponse response) {
		User u = userService.getUserByIdDetailed(userId);
		Credential c = new Credential();
		c.setAuthServiceType(cr.getAuthServiceType());
		c.setActive(true);
		c.setCreated(new DateTime());
		c.setUpdated(new DateTime());
		c.setUsername(cr.getUsername());
		c.setLink(cr.getLink());
		c.setOptData(cr.getOptData());
		userService.updatePasswordEmailCredential(c, cr.getEmail(), cr.getPassword());
		u.addCredential(c);
		userService.save(u);
		logger.info("CREDENTIAL ADDED: " + c + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("userId") Integer userId,
			@PathVariable("id") Integer id, 
			@RequestBody Credential cr, HttpServletRequest request, HttpServletResponse response) {
		Credential c = userService.getCredential(id);
		c.setAuthServiceType(cr.getAuthServiceType());
		c.setActive(cr.isActive());
		c.setUpdated(new DateTime());
		c.setUsername(cr.getUsername());
		c.setLink(cr.getLink());
		c.setOptData(cr.getOptData());
		userService.updatePasswordEmailCredential(c, cr.getEmail(), cr.getPassword());
		userService.saveCredential(c);
		logger.info("CREDENTIAL SAVED: " + c + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/chpassword")
	public CredentialPasswordForm getPasswordForm(@PathVariable("userId") Integer userId,
			@PathVariable("id") Integer id 
			) {
		Credential cr = userService.getCredential(id);
		CredentialPasswordForm f = new CredentialPasswordForm();
		f.setId(cr.getId());
		f.setEmail(cr.getEmail());
		f.setPassword("");
		f.setPasswordConfirm("");
		return f;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/chpassword")
	public void changePassword(@PathVariable("userId") Integer userId,
			@PathVariable("id") Integer id, 
			@RequestBody CredentialPasswordForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("data: " + form);
		Credential cr = userService.getCredential(id);
		if (form.getPassword().equals(form.getPasswordConfirm())) {
			userService.updatePasswordEmailCredential(cr, form.getEmail(), form.getPassword());
			userService.saveCredential(cr);
			logger.info("CREDENTIAL SAVED: " + cr + " Src:" + request.getRemoteAddr());
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			logger.info("CREDENTIAL SAVE error - password confirmation is not the same as the new password: " + form + " Src:" + request.getRemoteAddr());
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("userId") Integer userId, 
			@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
		Credential c = userService.getCredential(id);
		userService.deleteCredential(c);
		logger.info("CREDENTIAL DELETED: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}

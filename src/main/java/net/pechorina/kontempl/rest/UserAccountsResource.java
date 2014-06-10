package net.pechorina.kontempl.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.view.AbstractController;
import net.pechorina.kontempl.view.forms.UserFormNew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/users")
public class UserAccountsResource extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(UserAccountsResource.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(method = RequestMethod.GET) 
	public List<User> getUsers() {
		List<User> users = userService.findUsers();
		return users;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public User getById(@PathVariable("id") Integer id) {
		return userService.getUserByIdDetailed(id);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteById(@PathVariable("id") Integer id, HttpServletResponse response) {
		User u = userService.getUserById(id);
		userService.delete(u);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void addUser(@RequestBody UserFormNew userform, HttpServletRequest request, HttpServletResponse response) {
        User u = new User();
        u.setName(userform.getName());
        u.setActive(true);
        u.setLocked(false);
        u.setRoles(userform.getRoles());
        
        User newEntity = userService.saveNewUser(u, userform.getEmail(), userform.getPassword());
        
		logger.info("USER ADDED: " + newEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("id") Integer id, 
			@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		
		User entity = userService.save(user);

		logger.info("USER SAVED: " + entity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}

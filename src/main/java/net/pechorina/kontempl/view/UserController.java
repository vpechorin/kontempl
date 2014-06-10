package net.pechorina.kontempl.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.pechorina.kontempl.data.Credential;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.view.forms.CredentialPasswordForm;
import net.pechorina.kontempl.view.forms.UserForm;
import net.pechorina.kontempl.view.forms.UserFormNew;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
    @RequestMapping(value="/control", method=RequestMethod.GET)
    public String controlPage(Model model) {
        logger.debug("users control page");
        
        return "commons/userlist";
    }	
    
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public @ResponseBody ResultSet<User> list(Model model) {
        logger.debug("list users");
        List<User> l = userService.findUsers();
        if (l != null) {
        	logger.debug("Found: " + l.size() + " users");
        }
        else {
        	logger.warn("No users found");
        }
        ResultSet<User> rs = new ResultSet<User>(l);
        return rs;
    }	
	
    @RequestMapping(value="/credlist", method=RequestMethod.GET)
    public @ResponseBody ResultSet<Credential> credentialList(@RequestParam(value="userId", required=true) Integer userId, Model model) {
        logger.debug("list user credentialss");
        Set<Credential> crSet = userService.getUserCredentials(userId);
        List<Credential> l = new ArrayList<Credential>();
        if (crSet != null) {
        	l.addAll(crSet);
        }
        ResultSet<Credential> rs = new ResultSet<Credential>(l);
        return rs;
    }	
    
/*    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String add(Model model) {
        logger.debug("add user form");
        UserFormNew userForm = new UserFormNew();
        model.addAttribute("userform", userForm);
        Map<String,String> roleMap = roleService.getRoleMap();
        model.addAttribute("roles", roleMap);
        return "commons/useradd";
    }*/
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String add(@ModelAttribute("userform") UserFormNew userform,  BindingResult bindingResult, Model model) {
        logger.debug("add user form submit");
        if (bindingResult.hasErrors()) {
        	logger.debug("Input errors found");
            return "commons/useradd";
        } 
        User u = new User();
        u.setName(userform.getName());
        u.setActive(true);
        u.setLocked(false);
        u.setRoles(userform.getRoles());
        
        userService.saveNewUser(u, userform.getEmail(), userform.getPassword());
        
        return "redirect:/user/control";
    }
    
    @RequestMapping(value="/{userId}/edit", method=RequestMethod.GET)
    public String edit(@PathVariable("userId") int userId, Model model) {
        logger.debug("edit user form");
        User u = userService.getUserById(userId);
        UserForm userForm = new UserForm(u);
        model.addAttribute("u", u);
        model.addAttribute("userform", userForm);
        return "commons/useredit";
    }
    
    @RequestMapping(value="/{userId}/edit", method=RequestMethod.POST)
    public String editSave(@PathVariable("userId") int userId,
    		@ModelAttribute("userform") UserForm userForm,  BindingResult bindingResult,
    		Model model) {
        logger.debug("edit user form save");
    	
    	User u = userService.getUserById(userId);
    	model.addAttribute("u", u);

        if (bindingResult.hasErrors()) {
        	logger.debug("Input errors found");
            return "commons/useredit";
        } 
        
        u.setName(userForm.getName());
        u.setActive(userForm.getActive());
        u.setLocked(userForm.getLocked());
        
        userService.save(u);
        
        return "redirect:/user/control";
    }    
    
    @RequestMapping(value="/{userId}/crededit", method=RequestMethod.GET)
    public String editCredential(@PathVariable("userId") int userId,@RequestParam(value="id", required=true) Integer id, Model model) {
        logger.debug("edit user credential form");
        User u = userService.getUserById(userId);
        Credential c = userService.getUserCredentialById(id);
        model.addAttribute("user", u);
        model.addAttribute("userCredential", c);

        if (c.getAuthServiceType().equalsIgnoreCase("password")) {
        	CredentialPasswordForm f = new CredentialPasswordForm();
        	f.setEmail(c.getEmail());
        	f.setId(c.getId());
            model.addAttribute("credential", f);
        	return "commons/userpasswordedit";
        }
        return "redirect:/user/{userId}/edit";
    }
    
    @RequestMapping(value="/{userId}/crededit", method=RequestMethod.POST)
    public String editCredentialSave(@PathVariable("userId") int userId,
    		@ModelAttribute("credential") CredentialPasswordForm form,  BindingResult bindingResult,
    		Model model) {
        logger.debug("edit user credential form save");
        if (bindingResult.hasErrors()) {
        	logger.debug("Input errors found");
            return "commons/userpasswordedit";
        } 
        Credential c = userService.getUserCredentialById(form.getId());
        userService.updatePasswordEmailCredential(c, form.getEmail(), form.getPassword());
        return "redirect:/user/{userId}/edit";
    }   
    
    @RequestMapping(value="/{userId}/creddelete", method=RequestMethod.GET)
    public String deleteCredential(@PathVariable("userId") int userId, @RequestParam(value="id", required=true) Integer id, Model model) {
        logger.debug("delete user credential");
        Credential c = userService.getUserCredentialById(id);
        if (c != null) {
        	userService.deleteCredential(c);
        }
         return "redirect:/user/{userId}/edit";
    }
    
}

package net.pechorina.kontempl.view;

import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.Credential;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.view.forms.ProfileForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController extends AbstractController {
	private static final Logger logger = Logger.getLogger(ProfileController.class);
	
	@Autowired
	private UserService userService;
    
    @RequestMapping(value="/edit", method=RequestMethod.GET)
    public String editProfile(HttpSession session, Model model) {
        logger.debug("edit user profile form");
        User u = getUser();
        User user = userService.getUserById(u.getId());
        Credential c = userService.getUserCredential(u.getId(), "password");
        model.addAttribute("user", user);
        model.addAttribute("userCredential", c);

        if (c.getAuthServiceType().equalsIgnoreCase("password")) {
        	ProfileForm f = new ProfileForm();
        	f.setEmail(c.getEmail());
        	f.setName(user.getName());
            model.addAttribute("profile", f);
        	return "commons/profileedit";
        }
        return "redirect:/profile/edit";
    }
    
    @RequestMapping(value="/edit", method=RequestMethod.POST)
    public String saveProfile(@ModelAttribute("profile") ProfileForm form,  BindingResult bindingResult, HttpSession session, Model model) {
    	User u = getUser();
        logger.debug("edit user profile form save");
        if (bindingResult.hasErrors()) {
        	logger.debug("Input errors found");
            return "commons/profileedit";
        } 
        User user = userService.getUserById(u.getId());
        Credential c = userService.getUserCredential(user.getId(), "password");
        if (c != null) {
        	userService.updatePasswordEmailCredential(c, form.getEmail(), form.getPassword());
        }
    	
    	user.setName(form.getName());
    	userService.save(user);
    	
        // update user data in the session
        userService.setUserSession(request, user);
    	
        return "redirect:/profile/edit";
    }   
    
}

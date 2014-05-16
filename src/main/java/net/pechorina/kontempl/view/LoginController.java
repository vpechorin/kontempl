package net.pechorina.kontempl.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = "/do/signon", method = RequestMethod.GET)
	public String loginPage(Model model) {
		logger.debug("signon");
		return "commons/login";
	}

	@RequestMapping(value = "/do/loginfailed", method = RequestMethod.GET)
	public String loginerror(Model model) {
		model.addAttribute("error", "true");
		return "commons/login";
	}

	@RequestMapping(value = "/do/signoff", method = RequestMethod.GET)
	public String logout(Model model) {
		logger.debug("signoff");
		return "commons/login";

	}
}

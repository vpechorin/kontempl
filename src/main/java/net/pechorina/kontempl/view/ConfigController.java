package net.pechorina.kontempl.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConfigController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
    
    @RequestMapping(value = "/config/list")
    public String listConfig(Model model) {
        logger.debug("list config");
        model.addAttribute("message", appConfig.toString());
        return "commons/simplemessage";
    }

}

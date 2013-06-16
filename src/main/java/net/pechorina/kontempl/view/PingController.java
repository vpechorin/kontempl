package net.pechorina.kontempl.view;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PingController {
	private static final Logger logger = Logger.getLogger(PingController.class);
	
    @RequestMapping(value = "/ping")
    public String testCtrl(Model model) {
        logger.debug("ping controller");
        model.addAttribute("message", "PING-OK");
        return "commons/simplemessage";
    }

}

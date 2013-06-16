package net.pechorina.kontempl.view;

import net.pechorina.kontempl.service.SitemapService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("helperController")
@RequestMapping(value = "/do/helper")
public class HelperController extends AbstractController {
	private static final Logger logger = Logger.getLogger(HelperController.class);
    
	@Autowired
	SitemapService sitemapService;
	
    @RequestMapping(value="/submitsitemap")
    public String testSitemapSubmit(@RequestParam(value="sitemap", required=true) String sitemap, Model model) {
		model.addAttribute("message", "OK");
		logger.debug("Sitemap: " + sitemap);
        return "commons/simplemessage";
    }
    
    @RequestMapping(value="/submitsitemaphnd")
    public String testSitemapSubmitHnd( Model model) {
    	sitemapService.updateSitemap();
		model.addAttribute("message", "OK");
        return "commons/simplemessage";
    }
    
}

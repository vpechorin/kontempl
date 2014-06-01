package net.pechorina.kontempl.view;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.service.PageNavigationService;
import net.pechorina.kontempl.service.PageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    
	@Autowired
    private PageService pageService;
	
	@Autowired
	private PageNavigationService pageNavService;
	
    @RequestMapping(value="/v/{site}/index")
    public String indexPage(@PathVariable("site") String siteName, Model model) {    
        logger.debug("show index page");
        String pageName = env.getProperty("homePage");
        Page p = pageService.getPageCached(siteName, pageName);
        model.addAttribute("txt", p);
    	return "pages/pageindex";
    }

}

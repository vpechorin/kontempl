package net.pechorina.kontempl.view;

import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.utils.StringUtils;
import net.pechorina.kontempl.view.forms.SiteForm;

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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/site")
public class SiteController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(SiteController.class);

	
	@Autowired
	private SiteService siteService;
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String siteAdd( Model model ) {    
    	SiteForm f = new SiteForm();
        model.addAttribute("siteform", f);
        
        return "commons/siteedit";        
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String pageAddChild(@ModelAttribute SiteForm f, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("siteAddSubmit:: form=" + f);
        
        Site s = new Site();
        s.setName(StringUtils.clearPageName( f.getName() ));
        s.setTitle(f.getTitle());
        s.setDomain(f.getDomain());
        
        Site site = siteService.save(s);
        
        model.addAttribute("site", site);
        
        return "redirect:/site/edit/" + site.getId();
    } 
    
    @RequestMapping(value="/edit/{siteId}", method=RequestMethod.GET)
    public String siteEdit(@PathVariable("siteId") Integer siteId, Model model) {    
        logger.debug("siteEdit:: id=" + siteId);
        Site s = siteService.findById(siteId);
        SiteForm f = new SiteForm(s);
        
        model.addAttribute("siteform", f);
        model.addAttribute("site", s);
        
        return "commons/siteedit";
    }
    
    @RequestMapping(value="/edit/{siteId}", method=RequestMethod.POST)
    public String siteEditSubmit(@PathVariable("siteId") Integer siteId, 
            @ModelAttribute SiteForm f, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("siteId: " + siteId);
        logger.debug("siteEditSubmit:: form=" + f);
        
        Site s = siteService.findById(siteId);

        s.setName(StringUtils.clearPageName( f.getName() ));
        s.setDomain(f.getDomain());
        s.setTitle(f.getTitle());
        
        Site savedEntity = siteService.save(s);
        
        model.addAttribute("site", savedEntity);
        
        return "redirect:/site/edit/" + savedEntity.getId();
    }    
    
    @RequestMapping(value="/delete/{siteId}")
    public String siteDelete(@PathVariable("siteId") Integer siteId) {    
        logger.debug("delete site, siteId=" + siteId);
        Site s = siteService.findById(siteId);
        if (s != null) siteService.delete(s);
        return "redirect:/";
    }
    
    @RequestMapping(value="/asyncdelete/{siteId}")
    public @ResponseBody String siteDeleteAsync(@PathVariable("siteId") Integer siteId) {    
        logger.debug("delete site, siteId=" + siteId);
        Site s = siteService.findById(siteId);
        if (s != null) siteService.delete(s);
        return "OK";
    }
    
    @RequestMapping(value="/copy/{siteId}")
    public String siteCopy(@PathVariable("siteId") Integer siteId) {    
        logger.debug("copy site, source Id=" + siteId);
        logger.info("NOT IMPLEMENTED");
        return "redirect:/";
    }
    
}

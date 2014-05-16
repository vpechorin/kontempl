package net.pechorina.kontempl.view;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.exceptions.PageNotFoundException;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.utils.TextContentUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@RequestMapping(value = "/do/error")
public class ErrorController {
	static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
	
    @Autowired
    @Qualifier("appConfig")
    public java.util.Properties appConfig;
    
    @Autowired
    private PageService pageService;
    
	@Autowired
	private PageTreeService pageTreeService;
	
    @RequestMapping(value="/error", method=RequestMethod.GET)
    public ModelAndView errorPage(WebRequest webRequest, HttpServletRequest request) {
        // logger.error("general web error: " + webRequest);

    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("errors/404notfound");
    	ModelMap model = modelAndView.getModelMap();
    	fillModel(webRequest, model);
    	return modelAndView;
    }
	
    @RequestMapping(value="/notfound", method=RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String notFoundPage(WebRequest webRequest) {
        logger.error("notFound: " + webRequest);
        
        return "errors/404notfound";
    }
    
    @RequestMapping(value="/pagenotfound", method=RequestMethod.GET)
    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView pageNotFoundPage(PageNotFoundException ex, WebRequest webRequest) {
     	logger.error("pageNotFound: " + webRequest);
    	
     	ModelAndView modelAndView = new ModelAndView();
     	modelAndView.setViewName("errors/404pagenotfound");
     	ModelMap model = modelAndView.getModelMap();
     	fillModel(webRequest, model);
    	return modelAndView;
    }

    @RequestMapping(value="/internalservererror", method=RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView serverErrorPage(Exception ex, WebRequest webRequest) {
    	logger.error("internal server error: " + webRequest);
    	StringWriter stringWriter = new StringWriter();
    	PrintWriter printWriter = new PrintWriter(stringWriter);
    	ex.printStackTrace(printWriter);
    	logger.error(stringWriter.toString());
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.setViewName("errors/500error");
    	ModelMap model = modelAndView.getModelMap();
    	fillModel(webRequest, model);

    	return modelAndView; 
    }
    
    private User getUser() {
    	User user = null;
    	Authentication a = SecurityContextHolder.getContext().getAuthentication();
    	if (a != null) {
    		Object principal = a.getPrincipal();
    		if (principal != null) {
    			if (principal instanceof UserDetails) {
    				UserDetails userDetails = (UserDetails) principal;
    				OptiUserDetails s = (OptiUserDetails)userDetails;
    				user = s.getUser();
    			} 
    		}
    	}
    	
    	return user;
    }
    
    private void fillModel(WebRequest webRequest, ModelMap model) {
    	model.addAttribute("appVersion", appConfig.getProperty("application.build"));
    	model.addAttribute("appConfig", appConfig);
    	logger.debug("added appConfig to the model");

    	User user = this.getUser();
    	if (user != null) {
    		model.addAttribute("user", user);
    		logger.debug("added user to the model");
    	}

    	String httpProtocol = "http";
    	if(webRequest.isSecure()) {
    		httpProtocol = "https";
    	}
    	model.addAttribute("httpProtocol", httpProtocol);
		
		TextContentUtils tc = new TextContentUtils();
		model.addAttribute("textProcessor", tc);
	    
		PageTree pt = pageTreeService.getPublicPageTree();
		model.addAttribute("pageTree", pt);
		
		Page home = pageService.getPageWithInheritedElements(appConfig.getProperty("homePage"));
		model.addAttribute("homePage", home);
		
		model.addAttribute("isError", true);
    }
}

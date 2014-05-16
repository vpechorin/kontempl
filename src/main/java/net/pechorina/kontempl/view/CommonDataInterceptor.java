package net.pechorina.kontempl.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.OptiUserDetails;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.ProfilingService;
import net.pechorina.kontempl.service.UserService;
import net.pechorina.kontempl.utils.TextContentUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class CommonDataInterceptor implements WebRequestInterceptor  {
	static final Logger logger = LoggerFactory.getLogger(CommonDataInterceptor.class);
	
    @Autowired
    @Qualifier("appConfig")
    private java.util.Properties appConfig;
    
    @Autowired
    private ProfilingService profilingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private PageTreeService pageTreeService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void afterCompletion(WebRequest webRequest, Exception ex)
			throws Exception {
	}

	@Override
	public void postHandle(WebRequest webRequest, ModelMap model) throws Exception {
		logger.debug("CommonDataInterceptor::postHandle");
		if (model != null) {
//			logger.debug("==================================");
//			logger.debug("model1: " + model.toString());
//			logger.debug("==================================");
			
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
			if (!model.containsAttribute("httpProtocol")) {
				model.addAttribute("httpProtocol", httpProtocol);
			}
			
			TextContentUtils tc = new TextContentUtils();
			model.addAttribute("textProcessor", tc);
		    
			PageTree pt = pageTreeService.getPublicPageTree();
			model.addAttribute("pageTree", pt);
			
			Page home = pageService.getPageWithInheritedElements(appConfig.getProperty("homePage"));
			model.addAttribute("homePage", home);
			
//			logger.debug("==================================");
//			logger.debug("model: " + model.toString());
//			logger.debug("==================================");

		}
		else {
			logger.debug("model is NULL");
		}

	}

	@Override
	public void preHandle(WebRequest webRequest) throws Exception {
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
}

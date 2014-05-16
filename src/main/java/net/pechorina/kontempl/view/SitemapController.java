package net.pechorina.kontempl.view;

import net.pechorina.kontempl.service.SitemapService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SuppressWarnings("unused")
@Controller("sitemapController")
@RequestMapping(value = "/do/sitemap")
public class SitemapController extends AbstractController {

	static final Logger logger = LoggerFactory.getLogger(SitemapController.class);

	@Autowired
	SitemapService sitemapService;

	/*
	 * @RequestMapping(value="/testsubmit") public String
	 * testSitemapSubmit(@RequestParam(value="sitemap", required=true) String
	 * sitemap, Model model) { model.addAttribute("message", "OK");
	 * logger.debug("Sitemap: " + sitemap); return "commons/simplemessage"; }
	 */

	@RequestMapping(value = "/update")
	public String update(Model model) {
		sitemapService.onlyUpdateSitemap();
		model.addAttribute("message", "UPDATED");
		return "commons/simplemessage";
	}

	@RequestMapping(value = "/submit")
	public String submit(Model model) {
		sitemapService.submitSitemap();
		model.addAttribute("message", "SUBMIT OK");
		return "commons/simplemessage";
	}

}

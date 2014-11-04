package net.pechorina.kontempl.service

import net.pechorina.kontempl.data.Page
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.Site

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment

import groovy.util.logging.*

@Component
@Slf4j
class Prerender {
	@Autowired 
	private Environment env
	
	@Autowired
	private PageTreeService pageTreeService
	
	@Autowired
	private SiteService siteService
	
	@Scheduled(cron="* 10 23 * * *")
	public void scheduleSiteProcessing() {
		processSites()
	}
	
	public void processSites() {
		def sites = env.getProperty('prerenderSites').split(/,/)
		sites.each { 
			Site s = siteService.findByName(it)
			log.debug("Prerender site: ${s.getDomain()}")
			sitePrerender(s)
		}
	}
	
	public void sitePrerender(Site site) {
		String prerenderServer = env.getProperty('prerender.server')
		String snapshotsPath = env.getProperty('snapshots.path')
		String snapshotsSuffix = env.getProperty('snapshots.suffix')
		boolean useHtml5Urls = env.getProperty("useHtml5Urls", Boolean.class)
		String siteProto = env.getProperty('sitemapProto')
		def m = '/#!/pv/';
		if (useHtml5Urls) m = "/pv/"
		
		PageTree tree = pageTreeService.getPublicPageTree(site)
		
		// generate home page
		String uHome = "$siteProto://" + site.getDomain() + "/";
		def fHome = "$snapshotsPath/${site.getName()}/index$snapshotsSuffix"
		String urlHomeStr = prerenderServer + uHome
		renderAndSavePage(urlHomeStr, fHome)
		
		// generate other pages
		tree.listAllChildren().each { 
			Page p = it.getData();
			if ((p.getName() != site.getHomePage()) && (!p.isPlaceholder())) {
				def u = "$siteProto://" + site.getDomain() + m + p.getName()
				def f = "$snapshotsPath/${site.getName()}/${p.getName()}$snapshotsSuffix"
				String urlStr = prerenderServer + u.toString()
				renderAndSavePage(urlStr, f)
			}
		}
	}
	
	private void renderAndSavePage(String urlStr, String snapshotFilePath) {
		def url = new URL(urlStr)
		def outputFile = new File(snapshotFilePath)
		outputFile.delete()
		def connection = url.openConnection()
		connection.requestMethod = 'GET'
		if (connection.responseCode == 200) {
			log.debug("url fetch success: $urlStr")
			log.debug("File to save: $snapshotFilePath")
			outputFile.text = connection.content.text
			log.debug("File saved: " + snapshotFilePath)
		}
		else {
			log.error("An error occured: ${connection.responseCode} ${connection.responseMessage}")
		}
	}
}

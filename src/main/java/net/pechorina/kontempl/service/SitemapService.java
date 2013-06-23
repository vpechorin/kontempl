package net.pechorina.kontempl.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.utils.XmlUrl;
import net.pechorina.kontempl.utils.XmlUrlSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("sitemapService")
public class SitemapService {
	private static final Logger logger = Logger.getLogger(SitemapService.class);

	@Autowired
	private PageService pageService;
	
	@Autowired
	private PageTreeService pageTreeService;
    
	@Autowired
	@Qualifier("appConfig")
	public java.util.Properties appConfig;
	
	@Autowired
	@Qualifier("freemarkerCommonConfiguration")
	Configuration freemarkerCfg;

	public XmlUrlSet makeSitemap() {
    	logger.debug("make sitemap");
    	XmlUrlSet xmlUrlSet = new XmlUrlSet();
    	String domainName = appConfig.getProperty("domainname");
    	
    	// add homepage
    	XmlUrl xuRoot = new XmlUrl("http://" + domainName + "/", XmlUrl.Priority.TOP, "daily");
		xmlUrlSet.addUrl(xuRoot);
		
		PageTree tree = pageTreeService.getPublicPageTree();
		
		GenericTreeNode<Page> home = tree.findPageNode(appConfig.getProperty("homePage"));
		if (home != null) {
			auxAddPages(home, xmlUrlSet);
		}
		
		return xmlUrlSet;
	}
	
	private void auxAddPages(GenericTreeNode<Page> parent, XmlUrlSet xmlUrlSet) {
		String domainName = appConfig.getProperty("domainname");

		if (parent.hasChildren()) {
			for(GenericTreeNode<Page> child: parent.getChildren()) {
				if (!child.getData().isPlaceholder()) {
					String childUrl = "http://" + domainName + "/pv/" + child.getData().getName();
					XmlUrl childXu = new XmlUrl(childUrl, XmlUrl.Priority.HIGH, "daily");
					childXu.setLastmod(child.getData().lastModifiedDate());
					xmlUrlSet.addUrl(childXu);
				}

				auxAddPages(child, xmlUrlSet);
			}
		}
	}
	
    public void saveSitemapCompressed(XmlUrlSet xmlUrlSet) {
    	String sitemapFile = appConfig.getProperty("sitemapPath");
    	
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("urlset", xmlUrlSet);
		model.put("appConfig", appConfig);
		
        GZIPOutputStream out;
		try {
			out = new GZIPOutputStream(new FileOutputStream(sitemapFile));
			Writer w = new OutputStreamWriter(out);
			Template t = freemarkerCfg.getTemplate("commons/sitemap.ftl");
			Environment env = t.createProcessingEnvironment(model, w);
			env.setOutputEncoding(getCharset());
			env.process();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found: " + sitemapFile + " " + e);
		} catch (IOException e) {
			logger.error("IO error: " + sitemapFile + " " + e);
		} catch (TemplateException e) {
			logger.error("Template error: " + sitemapFile + " " + e);
		}
    }
    
    public void saveSitemapUncompressed(XmlUrlSet xmlUrlSet) {
    	String sitemapFile = appConfig.getProperty("sitemapPath");
    	
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("urlset", xmlUrlSet);
		model.put("appConfig", appConfig);
		
        FileOutputStream out;
		try {
			out = new FileOutputStream(sitemapFile);
			Writer w = new OutputStreamWriter(out);
			Template t = freemarkerCfg.getTemplate("commons/sitemap.ftl");
			Environment env = t.createProcessingEnvironment(model, w);
			env.setOutputEncoding(getCharset());
			env.process();
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			logger.error("File not found: " + sitemapFile + " " + e);
		} catch (IOException e) {
			logger.error("IO error: " + sitemapFile + " " + e);
		} catch (TemplateException e) {
			logger.error("Template error: " + sitemapFile + " " + e);
		}
    }
	
    private String getCharset() {
        String c = "ISO-8859-1";
        if (appConfig.containsKey("sitemapCharset")) {
            c = appConfig.getProperty("sitemapCharset");
        }
        return c;
    }
    
    public void onlyUpdateSitemap() {
    	XmlUrlSet s = makeSitemap();
    	
    	if (s != null) {
    		logger.debug(s.getXmlUrls().size() + " urls in sitemap");
    		if (appConfig.getProperty("sitemapCompressed").equalsIgnoreCase("yes")) {
    			saveSitemapCompressed(s);
    			logger.debug("gziped sitemap saved");
    		}
    		else {
    			saveSitemapUncompressed(s);
    		}
    		logger.debug("xml sitemap saved");
    	}
    }
    
    public void updateSitemap() {
    	XmlUrlSet s = makeSitemap();
    	
    	if (s != null) {
    		logger.debug(s.getXmlUrls().size() + " urls in sitemap");
    		if (appConfig.getProperty("sitemapCompressed").equalsIgnoreCase("yes")) {
    			saveSitemapCompressed(s);
    			logger.debug("gziped sitemap saved");
    		}
    		else {
    			saveSitemapUncompressed(s);
    		}
    		logger.debug("xml sitemap saved");
    		if (appConfig.getProperty("sitemapSubmitSw").equalsIgnoreCase("on")) {
    			submitSitemap();
    		}
    	}
    }
    
    public void submitSitemap() {
    	String sitemapLocation = "http://" + appConfig.getProperty("domainname") + appConfig.getProperty("sitemapUrl");
    	String url = "";
		try {
			url = appConfig.getProperty("sitemapSubmitUrl") + "?sitemap=" + URLEncoder.encode(sitemapLocation, "ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			logger.error("UnsupportedEncodingException: " + e1);
		}

		logger.debug("URL: " + url);
		HttpClient httpclient = new DefaultHttpClient();

    	try {
    		HttpGet httpget = new HttpGet(url);
    		
    		// Execute the method.
    		HttpResponse response = httpclient.execute(httpget);

    		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
    			logger.error("Submit failed: " + response.getStatusLine());
    		}
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            String responseStr = EntityUtils.toString(entity);
            logger.debug("Sitemap submit detailed response: " + responseStr);
    	} catch (IOException e) {
    		logger.error("Fatal IO error: " + e.getMessage());
    	} catch (ParseException e) {
    		logger.error("Fatal parse error: " + e.getMessage());    		
    	} finally {
    		// Release the connection.
    		httpclient.getConnectionManager().shutdown();
    	}  
    }
}

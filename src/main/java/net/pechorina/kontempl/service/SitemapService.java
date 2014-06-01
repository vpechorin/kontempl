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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("sitemapService")
public class SitemapService {
	static final Logger logger = LoggerFactory.getLogger(SitemapService.class);

	@Autowired
	private PageService pageService;
	
	@Autowired
	private PageTreeService pageTreeService;
    
	@Autowired
	private org.springframework.core.env.Environment env;
	
	@Autowired
	@Qualifier("freemarkerCommonConfiguration")
	Configuration freemarkerCfg;

	public XmlUrlSet makeSitemap() {
    	logger.debug("make sitemap");
    	XmlUrlSet xmlUrlSet = new XmlUrlSet();
    	String domainName = env.getProperty("domainname");
    	
    	// add homepage
    	XmlUrl xuRoot = new XmlUrl("http://" + domainName + "/", XmlUrl.Priority.TOP, "daily");
		xmlUrlSet.addUrl(xuRoot);
		
		//TO DO
		//PageTree tree = pageTreeService.getPublicPageTree();
		PageTree tree = null;
		
		GenericTreeNode<Page> home = tree.findPageNode(env.getProperty("homePage"));
		if (home != null) {
			auxAddPages(home, xmlUrlSet);
		}
		
		return xmlUrlSet;
	}
	
	private void auxAddPages(GenericTreeNode<Page> parent, XmlUrlSet xmlUrlSet) {
		String domainName = env.getProperty("domainname");

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
    	String sitemapFile = env.getProperty("sitemapPath");
    	
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("urlset", xmlUrlSet);
		model.put("env", env);
		
        GZIPOutputStream out;
		try {
			out = new GZIPOutputStream(new FileOutputStream(sitemapFile));
			Writer w = new OutputStreamWriter(out);
			Template t = freemarkerCfg.getTemplate("commons/sitemap.ftl");
			freemarker.core.Environment e = t.createProcessingEnvironment(model, w);
			e.setOutputEncoding(getCharset());
			e.process();
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
    	String sitemapFile = env.getProperty("sitemapPath");
    	
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("urlset", xmlUrlSet);
		model.put("env", env);
		
        FileOutputStream out;
		try {
			out = new FileOutputStream(sitemapFile);
			Writer w = new OutputStreamWriter(out);
			Template t = freemarkerCfg.getTemplate("commons/sitemap.ftl");
			freemarker.core.Environment e = t.createProcessingEnvironment(model, w);
			e.setOutputEncoding(getCharset());
			e.process();
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
        if (env.containsProperty("sitemapCharset")) {
            c = env.getProperty("sitemapCharset");
        }
        return c;
    }
    
    public void onlyUpdateSitemap() {
    	XmlUrlSet s = makeSitemap();
    	
    	if (s != null) {
    		logger.debug(s.getXmlUrls().size() + " urls in sitemap");
    		if (env.getProperty("sitemapCompressed").equalsIgnoreCase("yes")) {
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
    		if (env.getProperty("sitemapCompressed").equalsIgnoreCase("yes")) {
    			saveSitemapCompressed(s);
    			logger.debug("gziped sitemap saved");
    		}
    		else {
    			saveSitemapUncompressed(s);
    		}
    		logger.info("xml sitemap saved, pages: " + s.getXmlUrls().size());
    		if (env.getProperty("sitemapSubmitSw").equalsIgnoreCase("on")) {
    			submitSitemap();
    		}
    	}
    }
    
    public void submitSitemap() {
    	String sitemapLocation = "http://" + env.getProperty("domainname") + env.getProperty("sitemapUrl");
    	String url = "";
		try {
			url = env.getProperty("sitemapSubmitUrl") + "?sitemap=" + URLEncoder.encode(sitemapLocation, "ISO-8859-1");
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
            logger.info("Sitemap submitted to " + url);
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

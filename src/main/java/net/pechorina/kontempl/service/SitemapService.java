package net.pechorina.kontempl.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.W3CDateFormat.Pattern;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.Site;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class SitemapService {
    static final Logger logger = LoggerFactory.getLogger(SitemapService.class);

    @Autowired
    private PageTreeService pageTreeService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private Environment env;

    @Value("${sitemapProto}")
    private String sitemapProto;

    @Value("${pagePath}")
    private String pagePath;

    public void makeSitemap(boolean submit) {
        String sitemapDef = env.getProperty("sitemaps");
        Iterable<String> sitemapItems = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(sitemapDef);

        for (String item : sitemapItems) {
            Iterable<String> sitemapProps = Splitter.on(':').trimResults().split(item);
            String siteName = Iterables.getFirst(sitemapProps, null);
            String sitemapPath = Iterables.getLast(sitemapProps);
            Site site = siteService.findByName(siteName);
            makeSiteSitemap(site, sitemapPath, submit);
        }
    }

    private void makeSiteSitemap(Site site, String sitemapPath, boolean submit) {
        List<WebSitemapUrl> urls = makeUrlList(site);

        String siteUrl = sitemapProto + "://" + site.getDomain() + "/";

        W3CDateFormat dateFormat = new W3CDateFormat(Pattern.DAY);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        WebSitemapGenerator wsg = null;
        try {
            File path = new File(sitemapPath);
            wsg = WebSitemapGenerator.builder(siteUrl, path).dateFormat(dateFormat).build();
        } catch (MalformedURLException e) {
            logger.error("Bad site url: " + e);
        }
        assert wsg != null;
        wsg.addUrls(urls);
        wsg.write();
        if (submit) {
            try {
                submitSitemap(site);
            } catch (IOException e) {
                logger.error("Error submitting sitemap:" + e);
            }
        }
    }

    private List<WebSitemapUrl> makeUrlList(Site site) {
        logger.debug("make sitemap for " + site.getDomain());
        List<WebSitemapUrl> urls = new ArrayList<>();

        String domainName = site.getDomain();

        PageTree tree = pageTreeService.getPublicPageTree(site);

        String url = sitemapProto + "://" + domainName + "/";
        WebSitemapUrl homeUrl = null;
        try {
            homeUrl = new WebSitemapUrl.Options(url)
                    .lastMod(new Date()).priority(1.0).changeFreq(ChangeFreq.DAILY).build();
            urls.add(homeUrl);
        } catch (MalformedURLException e1) {
            logger.error("Error generating home page url: " + e1);
        }

        List<GenericTreeNode<Page>> nodes = tree.listAllChildren();
        for (GenericTreeNode<Page> node : nodes) {
            Page p = node.getData();
            if (p.getName().equalsIgnoreCase(site.getHomePage())) {
                continue;
            }
            if (p.getPlaceholder()) continue;
            WebSitemapUrl u = null;
            try {
                u = makeSitemapUrl(p, site);
                urls.add(u);
            } catch (MalformedURLException e) {
                logger.error("Bad url: " + e);
            }

        }

        return urls;
    }

    private WebSitemapUrl makeSitemapUrl(Page p, Site s) throws MalformedURLException {
        String u = sitemapProto + "://" + s.getDomain() + pagePath + p.getName();
        return new WebSitemapUrl.Options(u)
                .lastMod(p.getUpdated().toDate()).priority(0.9).changeFreq(ChangeFreq.WEEKLY).build();
    }

    public void onlyUpdateSitemap() {
        makeSitemap(false);
    }

    public void updateSitemap() {
        makeSitemap(true);
    }

    @Scheduled(cron = "0 30 4 * * MON-FRI")
    public void scheduledUpdate() {
        Boolean submit = env.getProperty("sitemapSubmit", Boolean.class);
        makeSitemap(submit);
    }

    public void submitSitemap(Site site) throws IOException {
        String sitemapLocation = sitemapProto + "://" + site.getDomain() + env.getProperty("sitemapUrl");
        String url = "";
        try {
            url = env.getProperty("sitemapSubmitUrl") + "?sitemap=" + URLEncoder.encode(sitemapLocation, "ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            logger.error("UnsupportedEncodingException: " + e1);
        }

        logger.debug("URL: " + url);
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
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
        }
        // Release the connection.

    }
}

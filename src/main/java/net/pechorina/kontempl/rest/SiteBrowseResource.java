package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageNode;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.api.PageDTO;
import net.pechorina.kontempl.data.api.SiteDTO;
import net.pechorina.kontempl.mappers.PageMapper;
import net.pechorina.kontempl.mappers.SiteMapper;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SiteBrowseResource {
    static final Logger logger = LoggerFactory.getLogger(SiteBrowseResource.class);

    @Autowired
    private SiteService siteService;

    @Autowired
    private PageService pageService;

    @Autowired
    private PageTreeService pageTreeService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/browse/sites/{siteName}")
    public ResponseEntity<SiteDTO> getSiteByName(@PathVariable("siteName") String siteName) {
        Site s = siteService.findByNameCached(siteName);
        return new ResponseEntity<>(SiteMapper.INSTANCE.toDTO(s), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/browse/sites/{siteName}/pages/{pageName}")
    public ResponseEntity<PageDTO> getPageByName(@PathVariable("siteName") String siteName,
                                                 @PathVariable("pageName") String pageName) {
        Site s = siteService.findByNameCached(siteName);
        Page p = pageService.getPageCached(s, pageName);
        return new ResponseEntity<>(PageMapper.INSTANCE.toPageDTO(p), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/browse/sites/{siteName}/pages")
    public ResponseEntity<List<PageDTO>> getSitePages(@PathVariable("siteName") String siteName) {
        Site s = siteService.findByNameCached(siteName);
        List<Page> pages = pageService.listBySite(s);
        return new ResponseEntity<>(pages.stream().map(p -> PageMapper.INSTANCE.toPageDTO(p)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/browse/sites/{siteName}/pages/{pageName}/children")
    public List<PageDTO> getPageChildrenByName(@PathVariable("siteName") String siteName,
                                               @PathVariable("pageName") String pageName) {
        Site s = siteService.findByNameCached(siteName);
        Page p = pageService.getPageCached(s, pageName);
        return pageService.listPageChildren(p).stream()
                .map(page -> PageMapper.INSTANCE.toPageDTO(page))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/browse/sites/{siteName}/tree")
    public List<PageNode> getPageTree(@PathVariable("siteName") String siteName,
                                      @RequestParam(value = "files", required = false) boolean includeFiles,
                                      @RequestParam(value = "images", required = false) boolean includeImages) {
        logger.debug("Site tree: " + siteName + ", images:" + includeImages + ", files:" + includeFiles);
        Site s = siteService.findByNameCached(siteName);
        return pageTreeService.getPageNodeTreePublic(s, includeImages, includeFiles);
    }
}

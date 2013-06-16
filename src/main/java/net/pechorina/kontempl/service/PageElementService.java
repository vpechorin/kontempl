package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.PageElement;
import net.pechorina.kontempl.repos.PageElementRepo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageElementService")
public class PageElementService {
    private static final Logger logger = Logger.getLogger(PageElementService.class);
    
    @Autowired 
    private PageElementRepo pageElementRepo;    
    
    @Transactional
    public void savePageElement(PageElement pe) {
    	pageElementRepo.save(pe);
    }
    
    @Transactional
    public void deletePageElement(PageElement pe) {
    	pageElementRepo.delete(pe);
    }
    
    @Transactional
    public PageElement getPageElementById(Integer id) {
        logger.debug("pageElement id:" + id);
        return pageElementRepo.findOne(id);
    }
}

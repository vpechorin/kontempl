package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.repos.SiteRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SiteService {
    static final Logger logger = LoggerFactory.getLogger(SiteService.class);

    @Autowired
    private SiteRepo siteRepo;

    @Transactional
    public List<Site> listAll() {
        return siteRepo.findAll();
    }

    @Transactional
    public Site findByName(String n) {
        return siteRepo.findByName(n);
    }

    @Transactional
    @Cacheable("siteCache")
    public Site findByNameCached(String n) {
        return siteRepo.findByName(n);
    }

    @Transactional
    public Site findById(Integer id) {
        return siteRepo.findOne(id);
    }

    @Transactional
    @CacheEvict(value = {"siteCache"}, allEntries = true)
    public Site save(Site s) {
        return siteRepo.saveAndFlush(s);
    }

    @Transactional
    @CacheEvict(value = {"siteCache"}, allEntries = true)
    public void delete(Site s) {
        siteRepo.delete(s);
    }

    @Transactional
    @CacheEvict(value = {"siteCache"}, allEntries = true)
    public void delete(Integer id) {
        siteRepo.delete(id);
    }

}

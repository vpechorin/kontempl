package net.pechorina.kontempl.config;

import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {
	
	@Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		
        CacheConfiguration pageCache = new CacheConfiguration();
        pageCache.setName("pageCache");
        pageCache.setTimeToLiveSeconds(864000);
        pageCache.setMaxEntriesLocalHeap(1000);
        config.addCache(pageCache);        
        
        CacheConfiguration siteCache = new CacheConfiguration();
        siteCache.setName("siteCache");
        siteCache.setTimeToLiveSeconds(864000);
        siteCache.setMaxEntriesLocalHeap(10);
        config.addCache(siteCache);

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}

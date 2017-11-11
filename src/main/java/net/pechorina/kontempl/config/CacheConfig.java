package net.pechorina.kontempl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        CaffeineCache pageCache = buildCache("pageCache", ticker, 14400, 100);
        CaffeineCache siteCache = buildCache("siteCache", ticker, 14400, 10);
        CaffeineCache treeCache = buildCache("treeCache", ticker, 14400, 4);
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(pageCache, siteCache, treeCache));
        return manager;
    }

    private CaffeineCache buildCache(String name, Ticker ticker, int minutesToExpire, int maxSize) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(minutesToExpire, TimeUnit.MINUTES)
                .maximumSize(maxSize)
                .ticker(ticker)
                .build());
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }

}

package com.batch.valuation.model;

//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * The Class FxMarketPricesStore.
 *
 * @author ashraf
 */
public class CacheUtil {
    private static Cache<String, StockVolume> _cache = null;

    public static Cache<String, StockVolume> getEhCache() {
        if (_cache == null) {
            CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                    .withCache("preConfigured",
                            CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, StockVolume.class, ResourcePoolsBuilder.heap(100_000))
                                    .build())
                    .build(true);

            _cache = cacheManager.createCache("stockVolume",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, StockVolume.class,
                            ResourcePoolsBuilder.heap(100_000)).build());
        }
        return _cache;
    }

//    public static Cache<String, StockVolume> getCaffeineCache() {
//        synchronized (CacheUtil.class) {
//            if (_cache == null)
//                _cache = Caffeine.newBuilder()
//                        .expireAfterWrite(2 * 60, TimeUnit.MINUTES)
//                        .maximumSize(1_000_000)
//                        .build();
//            else return _cache;
//        }
//        return _cache;

//        // Lookup an entry, or null if not found
//        Graph graph = cache.getIfPresent(key);
//        // Lookup and compute an entry if absent, or null if not computable
//        graph = cache.get(key, k -> createExpensiveGraph(key));
//        // Insert or update an entry
//        cache.put(key, graph);
//        // Remove an entry
//        cache.invalidate(key);
//    }

}

package com.demo.caffeine.service;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * author  :
 * time    :
 * description :
 */
@Service("T0PnLReconService")
public class T0PnLReconService {

    @Autowired
    @Qualifier("T0PnLCache")
    private Cache<String, String> t0PnLCache;
    @Autowired
    @Qualifier("T0PnLCacheManager")
    private CacheManager t0PnLCacheManager;

    public String getByRegion(String region) throws InterruptedException {
        System.out.println("Get data in region: " + region);
        if (Objects.isNull(t0PnLCache.getIfPresent(region))) {
            TimeUnit.SECONDS.sleep(1);
            t0PnLCache.put("APAC", "[T0PnL data in region] - APAC");
            t0PnLCache.put("EMEA", "[T0PnL data in region] - EMEA");
            t0PnLCache.put("NAM", "[T0PnL data in region] - NAM");
        }
        return t0PnLCache.getIfPresent(region);
    }

    @Cacheable(cacheNames = "T0PnLCache")
    public String getT0PnL_byCacheNames(String region) {
        System.out.println("Get T0PnL in Region: " + region);
        return "【T0PnL Data in Region】 - " + region;
    }

    @Cacheable(cacheNames = "T0PnLCache", key = "#region") // #id means id is the key to get value
    public String getT0PnL_byCacheNamesAndKey(Date date, String region) {
        System.out.println("Get T0PnL in Date: " + date + " for Region: " + region);
        org.springframework.cache.Cache t0Cache = t0PnLCacheManager.getCache("T0PnLCache");
        t0Cache.put("APAC", "Value-APAC");
        t0Cache.put("EMEA", "Value-EMEA");
        t0Cache.put("NAM", "Value-NAM");
        return t0Cache.get(region, String.class);
    }
}

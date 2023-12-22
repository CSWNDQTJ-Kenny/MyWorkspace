package com.demo.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * author  :
 * time    :
 * description :
 */

@Configuration
public class CacheCaffeine {

    @Resource(name = "WeightCache")
    private Cache weightCache;
    @Resource(name = "ExpireAfterAccessCache")
    private Cache expireAfterAccessCache;
    @Resource(name = "ExpireAfterWriteCache")
    private Cache expireAfterWriteCache;
    @Resource(name = "CustomExpiryTimeCache")
    private Cache customExpiryTimeCache;
    @Resource(name = "WeakReferenceCache")
    private Cache weakReferenceCache;
    @Resource(name = "SoftReferenceCache")
    private Cache softReferenceCache;
    @Resource(name = "RefreshAfterWriteCache")
    private Cache refreshAfterWriteCache;

}

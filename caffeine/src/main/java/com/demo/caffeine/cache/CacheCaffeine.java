package com.demo.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * author  :
 * time    :
 * description :
 */

public class CacheCaffeine {

    Cache<String, String> cache_01 = Caffeine.newBuilder()
            .maximumSize(100) //
            .build();

}

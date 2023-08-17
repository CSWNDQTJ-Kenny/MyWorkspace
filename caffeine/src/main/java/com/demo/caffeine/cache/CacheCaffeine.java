package com.demo.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * author  :
 * time    :
 * description :
 */

/**
 *  Caffeine组件包含有如下的使用特点：
 * （1）、可以自动将数据加载到缓存之中，也可以采用异步的方式进行加载，
 * （2）、当基于频率和最近访问的缓存达到最大容量时，该组件会自动切换到基于大小的模式；
 * （3）、可以根据上一次缓存访问或上一次的数据写入来决定缓存的过期处理；
 * （4）、当某一条缓存数据出现了过期访问后可以自动进行异步刷新，
 * （5）、考虑到JVM内存的管理机制，所有的缓存KEY自动包含在弱引用之中，VALUE包含在弱引用或软引用中，
 * （6）、当缓存数据被清理后，将会收到相应的通知信息；
 * （7）、缓存数据的写入可以传播到外部存储；
 * （8）、自动记录缓存数据被访问的次数。
 *
 */
public class CacheCaffeine {

    Cache<String, String> cache_01 = Caffeine.newBuilder()
            .maximumSize(100) //
            .build();

}

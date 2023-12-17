package com.demo.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * author  : Kenny
 * time    :
 * description : 创建 Caffeine 缓存，
 * Caffeine 缓存最大的优势在于提供了一套完整的淘汰机制
 * 基于需求，可有三种淘汰机制：
 * 基于大小
 * 基于权重
 * 基于时间
 * 基于引用（不常用）
 */
@Configuration
public class CaffeineCacheConfig {

    @Autowired
    @Qualifier("SimpleStringCacheLoader")
    private CacheLoader<String, String> simpleStringCacheLoader;

    @Autowired
    @Qualifier("FullMethodStringCacheLoader")
    private CacheLoader<String, String> fullMethodStringCacheLoader;

    /**
     * 基于大小：这意味着当缓存大小超过配置的大小限制时会发生回收
     * maximumSize基于 Window TinyLfu策略（类似LRU:最近最少使用原则）丢弃缓存
     */
    //@Primary
    @Bean("SizeCache")
    public Cache<String, String> sizeCache() {
        return Caffeine.newBuilder()
                .recordStats() //开启打点记录，用于统计命中率、被剔除的数量、加载新值所花费时间的平均值【纳秒】等
                .maximumSize(5)
                .build();
    }

    /**
     * 基于权重：这意味着当缓存大小超过配置的权重限制时会发生回收
     * maximumWeight基于Window TinyLfu策略（类似LRU:最近最少使用原则）丢弃缓存
     */
    @Bean("WeightCache")
    public Cache<Integer, Integer> weightCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .maximumWeight(3) //maximumWeight与weigher结合使用，当weigher中的权重值（weigher权重的初始值 * 缓存个数）超过maximumWeight的值时，新增记录的时候删除weigher值最小的缓存
                .weigher(new Weigher<Integer, Integer>() {
                    @Override
                    public /*@NonNegative*/ int weigh(Integer key, Integer value) {
                        return key; //这里使用key值作为权重
                    }
                })
                //.weigher((k,v)->1) //另一种写法记录一个键值对的权重
                .build();
    }

    /**
     * 基于时间 - 访问后到期
     * 时间节点从最近一次读或者写，也就是get或者put开始算起
     * <p>
     * 注意：
     * 1、构建Cache对象的时候都会调用scheduler(Scheduler.systemScheduler())，
     * Scheduler就是定期清空数据的一个机制，可以不设置，如果不设置则不会主动的清空过期数据。
     * 2、如果不设置Scheduler，那么数据会在我们进行操作的时候异步清空过期数据，也就是put或者get的时候
     * 3、Scheduler只有java9以上才会生效，即Caffeine主动清除过期缓存对JDK版本有要求
     */
    @Bean("ExpireAfterAccessCache")
    public Cache<String, String> expireAfterAccessCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterAccess(10, TimeUnit.SECONDS)
                //.scheduler(Scheduler.systemScheduler()) // 开启定期清空过期数据
                .build();
    }

    /**
     * 基于时间 - 写入后到期
     * 时间节点从写开始算起，也就是put
     */
    @Bean("ExpireAfterWriteCache")
    public Cache<String, String> expireAfterWriteCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .scheduler(Scheduler.systemScheduler()) // 开启定期清空过期数据
                .build();
    }

    /**
     * Test Scheduler for T0PnL Auto Recon Service
     * @return
     */
    @Bean("T0PnLCaffeineCache")
    public Caffeine<Object, Object> t0PnLCaffeineCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .maximumSize(10)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .scheduler(Scheduler.systemScheduler());// 开启定期清空过期数据
    }
    @Bean("T0PnLCache")
    public Cache<String, String> t0PnLCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .scheduler(Scheduler.systemScheduler()) // 开启定期清空过期数据
                .build(simpleStringCacheLoader);
    }



    /**
     * 基于时间 - 自定义策略
     * 自定义具体到期时间，使用Expiry自定义按时间失效方法
     */
    @Bean("CustomExpiryTimeCache")
    public Cache<String, String> expiryCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfter(new Expiry<String, String>() {
                    @Override
                    public long expireAfterCreate(String key, String value, long currentTime) {
                        return TimeUnit.SECONDS.toNanos(5); // 创建5秒后过期，这里必须要用纳秒
                    }

                    @Override
                    public long expireAfterUpdate(String key, String value, long currentTime, long currentDuration) {
                        return TimeUnit.SECONDS.toNanos(5); // 更新5秒后过期，这里必须要用纳秒
                    }

                    @Override
                    public long expireAfterRead(String key, String value, long currentTime, long currentDuration) {
                        return TimeUnit.SECONDS.toNanos(5); // 写5秒后过期，这里必须要用纳秒
                    }
                })
                //.scheduler(Scheduler.systemScheduler()) // 开启定期清空过期数据
                .build();
    }

    /**
     * 基于引用的的缓存淘汰机制，需要注意：
     * 1、System.gc() 不一定会真的触发GC
     * 2、使用异步加载的方式不允许使用引用淘汰机制，启动程序的时候会报错：java.lang.IllegalStateException: Weak or soft values can not be combined with AsyncCache
     * 3、使用引用淘汰机制的时候，判断两个key或者两个value是否相同，用的是 ==，而不是equals()，也就是说需要两个key指向同一个对象才能被认为是一致的，这样可能导致缓存命中出现预料之外的问题
     * 总结下来，慎用引用淘汰机制
     *
     * @return
     */
    @Bean("WeakReferenceCache")
    public Cache<String, String> weakReferenceCache() {
        return Caffeine.newBuilder()
                .weakKeys() // 设置Key为弱引用，生命周期是下次gc的时候
                .weakValues() // 设置value为弱引用，生命周期是下次gc的时候
                .build();
    }

    @Bean("SoftReferenceCache")
    public Cache<String, String> softReferenceCache() {
        return Caffeine.newBuilder()
                .softValues() // 设置value为软引用，生命周期是GC时并且堆内存不够时触发清除
                .build();
    }

    /**
     * 需求：需要用到写后一段时间定时过期，但这期间如果在一定时间内，数据有被访问，则重新计时，该如何实现？
     * 这是就需要用到 Caffeine 提供的刷新机制了 - refreshAfterWrite
     * 正常情况下 refreshAfterWrite 是和 expireAfterWrite 配套使用的
     * <p>
     * 注意：
     * 1、写后刷新其实并不是方法名描述的那样在一定时间后自动刷新，而是在一定时间后进行了访问，再访问后才自动刷新。
     * 2、在写后刷新被触发后，会重新填充数据，因而会触发写后过期时间机制的重新计算。
     *
     * @return
     */
    @Bean("RefreshAfterWriteCache")
    public Cache<String, String> refreshAfterWriteCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build(fullMethodStringCacheLoader);
    }


    /**
     * 充当二级缓存用，生命周期仅活到下个gc
     */
    private final Map<Integer, WeakReference<Integer>> secondCache = new ConcurrentHashMap<>();

    /**
     * 需求：Caffeine设置了最大缓存个数，会存在一个隐患，那便是到达最大缓存个数的情况下，会导致缓存被清，之后导致频繁读取数据库加载数据的情况，求一解决办法
     * 解决办法：在Caffeine的基础上，结合二级缓存解决此类问题
     * 这里引出 CacheWriter的概念，可以把它当做一个回调对象，在往Caffeine的缓存put数据或者remove数据的时候会触发该回调对象
     * 注：writer的监控是同步执行的
     */

    /*@Bean("WriterCache")
    public Cache<Integer, Integer> writerCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .maximumSize(1)
                .writer(new CacheWriter<Integer, Integer>() { // 设置put和remove的回调
                    @Override //若有新增缓存操作，这里的 write 方法将被执行，方法内会将当前准备写入 Caffeine 的数据写入自定义的缓存 secondCacheMap
                    public void write(@NonNull Integer key, @NonNull Integer value) {
                        secondCache.put(key, new WeakReference<>(value)); //写操作是阻塞的，写的时候读数据会返回原有值
                        System.out.println("触发CacheWriter.write，将key = " + key + "放入二级缓存中");
                    }

                    @Override //如果有缓存删除（到期等），这里的方法将被执行
                    public void delete(@NonNull Integer key, @NonNull Integer value, RemovalCause cause) {
                        switch (cause) {
                            case EXPLICIT:
                                secondCache.remove(key);
                                System.out.println("触发CacheWriter" + ".delete，清除原因：主动清除，将key = " + key + "从二级缓存清除");
                                break;
                            case SIZE:
                                System.out.println("触发CacheWriter" + ".delete，清除原因：缓存个数超过上限，key = " + key);
                                break;
                            default:
                                break;
                        }

                    }
                })
                .build(new CacheLoader<Integer, Integer>() {
                    @Nullable
                    @Override
                    public Integer load(@NonNull Integer key) {
                        WeakReference<Integer> value = secondCache.get(key);
                        if (value == null) {
                            return null;
                        }
                        System.out.println("触发CacheLoader.load，从二级缓存读取key = " + key);
                        return value.get();
                    }
                });
    }*/


    /**
     * 需求：如果数据忘记保存入库，然后被淘汰掉了，就会丢失数据，Caffeine有没有提供什么方法可以在淘汰数据的时候让开发者做点什么？
     * 答：有，Caffeine对外提供了淘汰监听，我们只需要在监听器内进行保存就可以了
     * RemovalListener的方法是异步执行的
     * <p>
     * 注意：这只能做个兜底机制，避免数据丢失
     */
    @Bean("RemovalListenerCache")
    public Cache<String, String> removalListenerCache() {
        return Caffeine.newBuilder()
                .recordStats()
                .refreshAfterWrite(5, TimeUnit.SECONDS)
                //.scheduler(Scheduler.systemScheduler())
                .removalListener(this::saveRemovalDataIntoDB)
                .build(fullMethodStringCacheLoader);
    }

    /**
     * 目前数据被淘汰的原因:
     * EXPLICIT：如果原因是这个，那么意味着数据被我们手动的remove掉了。
     * REPLACED：就是替换了，也就是put数据的时候旧的数据被覆盖导致的移除。
     * COLLECTED：这个有歧义点，其实就是收集，也就是垃圾回收导致的，一般是用弱引用或者软引用会导致这个情况。
     * EXPIRED：数据过期，无需解释的原因。
     * SIZE：个数超过限制导致的移除。
     *
     */
    private void saveRemovalDataIntoDB(String key, String value, RemovalCause cause) {
        System.out.println("Remove cache key: " + key + ", value: " + value + ", cause: " + cause);
        System.out.println("Can save removal data in to DB here!");
    }

}

Caffeine组件包含有如下的使用特点:
* 1、可以自动将数据加载到缓存之中，也可以采用异步的方式进行加载，
* 2、当基于频率和最近访问的缓存达到最大容量时，该组件会自动切换到基于大小的模式；
* 3、可以根据上一次缓存访问或上一次的数据写入来决定缓存的过期处理;
* 4、当某一条缓存数据出现了过期访问后可以自动进行异步刷新,
* 5、考虑到JVM内存的管理机制,所有的缓存KEY自动包含在弱引用之中,VALUE包含在弱引用或软引用中,
* 6、当缓存数据被清理后,将会收到相应的通知信息;
* 7、缓存数据的写入可以传播到外部存储;
* 8、自动记录缓存数据被访问的次数。

/**
  注解：
* cacheNames/value ：用来指定缓存组件的名字
* key ：缓存数据时使用的 key，可以用它来指定。默认是使用方法参数的值。（这个 key 你可以使用 spEL 表达式来编写）
* keyGenerator ：key 的生成器。 key 和 keyGenerator 二选一使用
* cacheManager ：可以用来指定缓存管理器。从哪个缓存管理器里面获取缓存。
* condition ：可以用来指定符合条件的情况下才缓存
* unless ：否定缓存。当 unless 指定的条件为 true ，方法的返回值就不会被缓存。当然你也可以获取到结果进行判断。（通过 #result 获取方法结果）
* sync ：是否使用异步模式。
  */

/**

* 名字	            位置	                描述	                                                                            示例
* methodName	    root object	        当前被调用的方法名	                                                                #root.methodName
* method	        root object	        当前被调用的方法	                                                                #root.method.name
* target	        root object	        当前被调用的目标对象	                                                            #root.target
* targetCIass	    root object	        当前被调用的目标对象类	                                                            #root.targetClass
* args	            root object	        当前被调用的方法的参数列表	                                                        #root.args[0]
* caches	        root object	        当前方法调用使用的缓存列表（如@Cacheable(value={"cache1","cache2"})，则有两个cache)	#root.caches[0].name
* argument name	evaluation context	方法参数的名字，可以直接使用#参数名，也可以使用 #p0或 #a0的形式，0代表参数的索引。	        #a0
* result	        evaluation context	方法执行后的返回值（仅当方法执行后的判断有效）	                                        #result
  */


// Case 01
@Test
void sync_case_01() throws Exception { // 测试Caffeine组件的基本操作
Cache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.build();
cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
TimeUnit.SECONDS.sleep(5); // 5秒后超时
System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
}

// Case 02
@Test
void sync_case_02() throws Exception { // 测试Caffeine组件的基本操作
Cache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.build();
cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
TimeUnit.SECONDS.sleep(5); // 5秒后超时
System.out.println("【已超时获取缓存数据】Kenny = " + cache.get("Kenny",
(key) -> {
System.out.println("【失效处理】没有发现 KEY = " + key + " 的数据，要进行失效处理控制。");
try {
TimeUnit.SECONDS.sleep(2);
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
return "【EXPIRE】" + key; // 失效数据的返回
})); // 获取数据
}

// Case 03
@Test
void sync_case_03() throws Exception { // 测试Caffeine组件的基本操作
LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.build(new CacheLoader<String, String>() {
@Override
public @Nullable String load(String key) throws Exception {
System.out.println("【CacheLoader】进行缓存数据的加载。。。");
TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
return "【LoadingCache】" + key; // 数据加载的返回结果
}
});

    cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
    System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    TimeUnit.SECONDS.sleep(5); // 5秒后超时
    cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
    for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
        System.out.println("【已超时获取缓存数据, 数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
    }

}

// Case 04
@Test
void sync_case_04() throws Exception { // 测试Caffeine组件的基本操作
LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.build(new CacheLoader<String, String>() {
@Override
public @Nullable String load(String key) throws Exception {
System.out.println("【CacheLoader】进行缓存数据的加载,当前的KEY = " + key);
TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
return "【LoadingCache】" + key; // 数据加载的返回结果
}
});

    cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
    System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    TimeUnit.SECONDS.sleep(5); // 5秒后超时
    cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
    for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
        System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
    }
    System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
}

// Case 05
*//**
* 相比之前的 get() 方法同步加载数据，此时使用专属的功能接口完成了数据加载，更加标准化
* @throws InterruptedException
*//*
@Test
void sync_case_05() throws InterruptedException {
LoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.build((key) -> {
System.out.println("【CacheLoader】进行缓存数据的加载,当前的KEY = " + key);
TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
return "【LoadingCache】" + key; // 数据加载的返回结果

            }); //构建 Cache接口实例

    cache.put("Kenny", "cswndqtj@sina.com"); // 设置缓存项
    System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
    TimeUnit.SECONDS.sleep(5); // 5秒后超时
    cache.put("edu", "edu.heima.com"); // 缓存失效之后 继续追加新的数据
    for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).entrySet()) { // 重新加载缓存
        System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
    }
    System.out.println("【已超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny")); // 获取数据
}


// Case 06
@Test
void async_case_01() throws ExecutionException, InterruptedException {
AsyncLoadingCache<String, String> cache = Caffeine.newBuilder() // 构建一个新的Caffeine实例
.maximumSize(100) // 设置缓存之中保存的最大数据量
.expireAfterAccess(3L, TimeUnit.SECONDS) // 如无访问 则3秒后失效
.buildAsync((key, executor) ->
CompletableFuture.supplyAsync(() -> {
System.out.println("【AsyncCacheLoader】进行缓存数据的加载,当前的KEY = " + key);
try {
TimeUnit.SECONDS.sleep(2); // 模拟数据的加载延迟
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
return "【AsyncCacheLoader】" + key; // 数据加载的返回结果

                })
            ); //构建 Cache接口实例
    // 此时是一个异步的缓存管理，所以在这个时候进行数据追加处理时，也要采用异步的方式
    cache.put("Kenny", CompletableFuture.completedFuture("cswndqtj@sina.com")); // 追加缓存项
    System.out.println("【未超时获取缓存数据】Kenny = " + cache.getIfPresent("Kenny").get()); // 获取数据
    TimeUnit.SECONDS.sleep(5); // 5秒后超时
    cache.put("edu", CompletableFuture.completedFuture("edu.heima.com")); // 缓存失效之后 继续追加新的数据
    for (Map.Entry<String, String> entry : cache.getAll(List.of("Kenny", "edu")).get().entrySet()) { // 重新加载缓存
        System.out.println("【数据加载】key = " + entry.getKey() + ", value = " + entry.getValue()); // 加载数据
    }
}
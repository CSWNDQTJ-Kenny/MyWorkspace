Caffeine组件包含有如下的使用特点:
1、可以自动将数据加载到缓存之中，也可以采用异步的方式进行加载，
2、当基于频率和最近访问的缓存达到最大容量时，该组件会自动切换到基于大小的模式；
3、可以根据上一次缓存访问或上一次的数据写入来决定缓存的过期处理;
4、当某一条缓存数据出现了过期访问后可以自动进行异步刷新,
5、考虑到JVM内存的管理机制,所有的缓存KEY自动包含在弱引用之中,VALUE包含在弱引用或软引用中,
6、当缓存数据被清理后,将会收到相应的通知信息;
7、缓存数据的写入可以传播到外部存储;
8、自动记录缓存数据被访问的次数。

Spring Cache


SpEL表达式：
名字	            位置	                描述	                                                                            示例
methodName	    root object	        当前被调用的方法名	                                                                #root.methodName
method	        root object	        当前被调用的方法	                                                                #root.method.name
target	        root object	        当前被调用的目标对象	                                                            #root.target
targetCIass	    root object	        当前被调用的目标对象类	                                                            #root.targetClass
args	            root object	        当前被调用的方法的参数列表	                                                        #root.args[0]
caches	        root object	        当前方法调用使用的缓存列表（如@Cacheable(value={"cache1","cache2"})，则有两个cache)	#root.caches[0].name
argument name	evaluation context	方法参数的名字，可以直接使用#参数名，也可以使用 #p0或 #a0的形式，0代表参数的索引。	        #a0
result	        evaluation context	方法执行后的返回值（仅当方法执行后的判断有效）	                                        #result

```java

```
package com.demo.caffeine.service;

import com.demo.caffeine.entity.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

/**
 * author  :
 * time    :
 * description :
 * <p>
 * Method Annotation: @Cacheable、@CachePut、@CacheEvict、@Caching
 * These annotation can use upon a method
 * <p>
 * @Cacheable 该注解表示这个方法有了缓存的功能，方法的返回值会被缓存下来，下一次调用该方法前，会去检查是否缓存中已经有值，如果有就直接返回，不调用方法。如果没有，就调用方法，然后把结果缓存起来。这个注解一般用在查询方法上。
 * 注：@Cacheable 注解下的 所有参数 均是 非必须选项
 * 参数：
 * cacheNames/value ：用来指定使用哪个缓存组件，即缓存的名字，两个参数等价，cacheNames为Spring 4新增，作为value的别名，又由于Spring 4中新增了@CacheConfig，因此在Spring 3中原本必须有的value属性，也成为非必需项了
 * key ：代表缓存数据时使用的 key，和 cacheNames共同组成一个唯一标识，即在哪个名字的缓存中获取哪个值。默认按照函数的所有参数组合作为key值，也可以自己使用 spEL 表达式来编写key值，spEL见下文
 * keyGenerator ：key的生成器，帮助我们按照自定义的要求生成 key，该参数与key是互斥的，即 key 和 keyGenerator二选一使用。需要实现org.springframework.cache.interceptor.KeyGenerator接口，并使用该注解参数来指定。
 * condition ：可以用来指定缓存对象的条件，需使用 SpEL表达式，只有满足表达式条件的内容才会被缓存。在函数调用前进行判断，因此 result这种变量在 SpEL里面进行判断时永远为 null。用法举例：@Cacheable(key = "#p0", condition = "#p0.length() < 3")，表示只有当第一个参数的长度小于 3的时候才会被缓存
 * unless ：另外一个缓存条件参数，需使用 SpEL表达式，该条件是在函数被调用之后才做判断的，所以它可以通过对 #result进行判断。当 unless 指定的条件为 true ，方法的返回值就不会被缓存。
 * cacheManager ：可以用来指定缓存管理器，只有当有多个缓存管理器时才需要使用，指明从哪个缓存管理器里面获取缓存。
 * cacheResolver ：用于指定使用那个缓存解析器，需通过org.springframework.cache.interceptor.CacheResolver接口来实现自己的缓存解析器
 * sync ：是否使用异步模式。
 * <p>
 * spEL 表达式
 * 名字	            位置	                描述	                                                                            示例
 * methodName	    root object	        当前被调用的方法名	                                                                #root.methodName
 * method	        root object	        当前被调用的方法	                                                                #root.method.name
 * target	        root object	        当前被调用的目标对象	                                                            #root.target
 * targetCIass	    root object	        当前被调用的目标对象类	                                                            #root.targetClass
 * args	            root object	        当前被调用的方法的参数列表	                                                        #root.args[0]
 * caches	        root object	        当前方法调用使用的缓存列表（如@Cacheable(value={"cache1","cache2"})，则有两个cache)	#root.caches[0].name
 * argument name	evaluation context	方法参数的名字，可以直接使用#参数名，也可以使用 #p0或 #a0的形式，0代表参数的索引。	        #a0
 * result           evaluation context  方法执行后的返回值，仅当方法执行后 判断有效                                             #result
 */

/*
 * Method Annotation: @Cacheable、@CachePut、@CacheEvict、@Caching
 * These annotation can use upon a method
 */


/**
 * @CachePut 该注解能够根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用，一般配置在方法上
 */

/**
 * @CachEvict 该注解能够根据一定的条件对缓存进行清空，一般配置在方法上
 */

/**
 * @Caching 该注解可以组合多个Cache注解使用。因为Java注解的机制决定了，一个方法上只能有一个相同的注解生效，那有时候可能一个方法会操作多个缓存，这时该注解就登场了
 */

@Service
public class CaffeineService {

    public Employee getEmployee(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps")
    public Employee getEmployee_byCacheNames(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = {"emps", "caffeine"}) // also can name multiple caches
    public Employee getEmployee_byCacheNamesList(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#id") // #id means id is the key to get value
    public Employee getEmployee_byCacheNamesAndKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#root.methodName + '[' + #id + ']'")
    public Employee getEmployee_byCacheNamesAndSpELKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", keyGenerator = "myKeyGenerator") // This myKeyGenerator is customed by ourselves
    public Employee getEmployee_byCacheNamesAndKeyGenerator(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", condition = "#id > 1") // When id > 1, then save the result into cache
    public Employee getEmployee_byCacheNamesAndCondition(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", unless = "#id > 1") // When id <= 1, then save the result into cache
    public Employee getEmployee_byCacheNamesAndUnless(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @CachePut(cacheNames = "emps", key = "#id")
    public Employee updateEmployee_byCacheNamesAndKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @CacheEvict(cacheNames = "emps", key = "#id")
    public void deleteEmployee_byCacheNamesAndKey(String id) {
        System.out.println("Delete " + id);
        System.out.println("*** Already deleted ID: " + id);
    }

    @Caching(put = {
            @CachePut(cacheNames = "emps", key = "#employee.id"),
            @CachePut(cacheNames = "emps", key = "#employee.name"),
            @CachePut(cacheNames = "emps", key = "#employee.age")
    })
    public Employee updateEmployee_byCacheNamesAndKey(Employee employee) {
        System.out.println("Get " + employee.getId());
        return new Employee("1", "Kenny", 30);
    }

}

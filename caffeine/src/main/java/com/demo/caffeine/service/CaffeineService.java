package com.demo.caffeine.service;

import com.demo.caffeine.entity.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * author  :
 * time    :
 * description :
 */


/**
 * 注解：
 * cacheNames/value ：用来指定缓存组件的名字
 * key ：缓存数据时使用的 key，可以用它来指定。默认是使用方法参数的值。（这个 key 你可以使用 spEL 表达式来编写）
 * keyGenerator ：key 的生成器。 key 和 keyGenerator 二选一使用
 * cacheManager ：可以用来指定缓存管理器。从哪个缓存管理器里面获取缓存。
 * condition ：可以用来指定符合条件的情况下才缓存
 * unless ：否定缓存。当 unless 指定的条件为 true ，方法的返回值就不会被缓存。当然你也可以获取到结果进行判断。（通过 #result 获取方法结果）
 * sync ：是否使用异步模式。
 *
 *
 * spEL 表达式
 * 名字	            位置	                描述	                                                                            示例
 * methodName	    root object	        当前被调用的方法名	                                                                #root.methodName
 * method	        root object	        当前被调用的方法	                                                                #root.method.name
 * target	        root object	        当前被调用的目标对象	                                                            #root.target
 * targetCIass	    root object	        当前被调用的目标对象类	                                                            #root.targetClass
 * args	            root object	        当前被调用的方法的参数列表	                                                        #root.args[0]
 * caches	        root object	        当前方法调用使用的缓存列表（如@Cacheable(value={"cache1","cache2"})，则有两个cache)	#root.caches[0].name
 * argument name	evaluation context	方法参数的名字，可以直接使用#参数名，也可以使用 #p0或 #a0的形式，0代表参数的索引。	        #a0
 * result	        evaluation context	方法执行后的返回值（仅当方法执行后的判断有效）	                                        #result
 *
 */
@Service
public class CaffeineService {

    public Employee getEmployee(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#id") // #id means id is the key to get value
    public Employee getEmployee_byCacheNamesAndKey(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = {"emps", "caffeine"}) // also can name multiple caches
    public Employee getEmployee_byCacheNames(String id) {
        System.out.println("Get " + id);
        return new Employee("1", "Kenny", 30);
    }

    @Cacheable(cacheNames = "emps", key = "#root.methodName + '[' + #id + ']'")
    public Employee getEmployee_byKey(String id) {
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

}

package com.lhw.apply.service;

import com.lhw.apply.dao.SessionUserRepository;
import com.lhw.apply.model.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ：linhw
 * @date ：22.3.16 14:33
 * @description：测试@Cacheable注解
 *
 *      缓存中key命名格式：cacheNames::key
 *
 *      方法执行前先看缓存中是否有数据，如果有直接返回。如果没有就调用方法，并将方法返回值放入缓存
 *
 *      注意：
 *          1、如果只是设置了cacheNames，没有设置key值，则它的缓存名为 cacheNames::returnValue
 *              如下save方法，在redis中的缓存key名称为：cacheable::SessionUser(userId=2a432e7a-ef53-4086-99ba-dea6ee714d4a, name=lhw, age=48)
 *          2、使用该注解存储的缓存，过期时间和CacheManager有关
 *              具体的可以看到base模块中的RedisConfig的CacheManager配置。（我是配置成了60秒）
 *
 *
 *      cacheNames 缓存名称
 *      value 缓存名称的别名
 *      condition Spring SpEL 表达式，用来确定是否缓存
 *      key SpEL 表达式，用来动态计算key
 *      keyGenerator Bean 名字，用来自定义key生成算法，跟key不能同时用
 *      unless SpEL 表达式，用来否决缓存，作用跟condition相反
 *      sync 多线程同时访问时候进行同步
 *
 * @modified By：
 */
@Service
public class CacheableTestImpl {

    private static final Logger LOG = LoggerFactory.getLogger(CacheableTestImpl.class);

    @Autowired
    SessionUserRepository repository;

    /**
     * 取第一个入参作为key
     *      在redis中的缓存key名为：cacheable::f3292c51-e1a5-4904-b812-18581e871b0f
     *      这里的f3292c51-e1a5-4904-b812-18581e871b0f为SessionUser的userId值
     *
     * 其他SPEL表达式如下：
     * #root.method 当前方法
     * #root.target 目标对象
     * #root.caches 被影响到的缓存列表
     * #root.methodName 方法名称简称
     * #root.targetClass 目标类
     * #root.args[x] 方法的第x个参数
     * @param id
     * @return
     */
//    @Cacheable(cacheNames = "cacheable", key = "#root.args[0]")
    @Cacheable(cacheNames = "cacheable", key = "#p0")
    public SessionUser findById(String id) {
        LOG.info("缓存中不存在【{}】，开始查询数据库",id);
        return repository.findById(id).orElse(new SessionUser());
    }

    /**
     * 缓存key名：cacheable::10-45
     *
     * @param start
     * @param end
     * @return
     */
    @Cacheable(cacheNames = "cacheable", key = "T(String).valueOf(#start).concat('-').concat(#end)")
    public List<SessionUser> findByAgeBetween(int start, int end) {
        return repository.findByAgeBetween(start,end);
    }

    /**
     * 普通的插入
     * @param sessionUser
     * @return
     */
    @Cacheable(cacheNames = "cacheable")
    public SessionUser save(SessionUser sessionUser){
        repository.save(sessionUser);
        return sessionUser;
    }

    /**
     * #p0指代的是第一个入参
     *      和#root.args[0]一样的
     *
     *      #px 等价于  #root.args[x]
     *
     * @return
     */
    @Cacheable(cacheNames = "cacheable", key = "#p0.userId")
    public SessionUser saveId(SessionUser sessionUser){
        repository.save(sessionUser);
        return sessionUser;
    }

    /**
     * 按条件进行缓存，当age是偶数的时候再缓存
     *      这里#sessionUser同样等价于上面的#p0
     *      可以直接拿到入参
     * @param sessionUser
     * @return
     */
    @Cacheable(cacheNames = "cacheable", key = "#sessionUser.userId", condition = "#sessionUser.age % 2 == 0")
    public SessionUser saveCondition(SessionUser sessionUser){
        repository.save(sessionUser);
        return sessionUser;
    }

}

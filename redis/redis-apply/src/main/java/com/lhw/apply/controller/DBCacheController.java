package com.lhw.apply.controller;

import com.lhw.apply.model.SessionUser;
import com.lhw.apply.service.CacheableTestImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @author ：linhw
 * @date ：22.3.16 11:37
 * @description：数据库缓存控制器
 * @modified By：
 */
@RestController
@RequestMapping("/db")
public class DBCacheController {

    @Autowired
    CacheableTestImpl cacheableTest;

    /**
     * 普通插入
     *      以返回结果为key进行缓存
     *      缓存名为：cacheable::SessionUser(userId=2a432e7a-ef53-4086-99ba-dea6ee714d4a, name=lhw, age=48)
     * @return
     */
    @RequestMapping(value = "/cacheable/save", method = RequestMethod.GET)
    public SessionUser save(){
        return cacheableTest.save(init());
    }

    /**
     * 根据ID作为key
     *      把入库的SessionUser的userId作为key进行缓存
     *      缓存名为：cacheable::2a432e7a-ef53-4086-99ba-dea6ee714d4a
     * @return
     */
    @RequestMapping(value = "/cacheable/saveId", method = RequestMethod.GET)
    public SessionUser saveId(){
        return cacheableTest.saveId(init());
    }

    /**
     * 根据条件进行缓存
     *      当SessionUser的age是偶数的时候才缓存
     *      缓存名为：cacheable::2a432e7a-ef53-4086-99ba-dea6ee714d4a
     * @return
     */
    @RequestMapping(value = "/cacheable/saveCondition", method = RequestMethod.GET)
    public SessionUser saveCondition(){
        return cacheableTest.saveCondition(init());
    }

    @RequestMapping(value = "/cacheable/findById/{id}", method = RequestMethod.GET)
    public SessionUser findById(@PathVariable String id){
        return cacheableTest.findById(id);
    }

    /**
     * 查询指定年龄范围的结果进行缓存
     *      当start = 10，end = 45时
     *      缓存名：cacheable::10-45
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/cacheable/findByAge/{start}/{end}", method = RequestMethod.GET)
    public List<SessionUser> findByAge(@PathVariable Integer start, @PathVariable Integer end){
        return cacheableTest.findByAgeBetween(start,end);
    }

    SessionUser init(){
        Random random = new Random();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setName("lhw");
        sessionUser.setAge(random.nextInt(50));
        return sessionUser;
    }

}

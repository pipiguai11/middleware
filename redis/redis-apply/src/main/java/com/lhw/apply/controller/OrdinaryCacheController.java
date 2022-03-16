package com.lhw.apply.controller;

import com.lhw.redisbase.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author ：linhw
 * @date ：22.3.16 10:48
 * @description：缓存测试控制器
 * @modified By：
 */
@RestController
@RequestMapping("/ordinary")
public class OrdinaryCacheController {

    //注意：想要这个logger生效，必须配置log4j.properties配置文件
    private Logger logger = LoggerFactory.getLogger(OrdinaryCacheController.class);

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 字符串缓存
     * @param cacheName
     * @return
     */
    @RequestMapping(value = "/string/{cacheName}" , method = RequestMethod.GET)
    public String string(@PathVariable String cacheName){
        String defaultValue = "default";
        Object result = redisUtil.get(cacheName);
        if (Objects.isNull(result)){
            logger.warn("{} cache is not exist;", cacheName);
            redisUtil.set(cacheName, defaultValue);
            return defaultValue;
        }
        return result.toString();
    }

    @RequestMapping(value = "/hash/{cacheName}", method = RequestMethod.GET)
    public String hash(@PathVariable String cacheName){
        return null;
    }

}

package com.lhw.apply;

import com.lhw.apply.model.SessionUser;
import com.lhw.apply.service.CacheableTestImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class RedisApplyApplicationTests {

    @Autowired
    CacheableTestImpl cacheableTest;

    @Test
    void contextLoads() {

        cacheableTest.save(init());
//        cacheableTest.findById("");

    }

    SessionUser init(){
        Random random = new Random();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setName("lhw");
        sessionUser.setAge(random.nextInt(50));
        return sessionUser;
    }

}

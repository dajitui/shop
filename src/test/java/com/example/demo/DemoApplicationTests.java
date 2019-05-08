package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    ShopDao shopDao;

    @Test
    public void contextLoads() {
        ShopDO shopDO=new ShopDO();
        shopDO.setName("哈哈");
        shopDO.setPoint(0);

        shopDao.save(shopDO);
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RocketMQProvider provider;

    @Autowired
    Service service;

    @Autowired
    Rlock rlock;

    @Test
    public void a(){
        System.out.println(rlock.lock("1"));
        System.out.println(rlock.unlock("1"));
    }

}

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created on 2019/5/7.
 *
 * @author yangsen
 */
@org.springframework.stereotype.Service
@Component
public class IServiceImpl implements Service {

    @Autowired
    RedisTemplate redisTemplate;

    static ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue(100);

    @Override
    public Integer shop(String name) {
        double result = redisTemplate.opsForValue().increment("1", 1);
        if (result > 1500) {
            return -1;
        } else {
            try {
                arrayBlockingQueue.put(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return 1;
    }

    @Autowired
    Rlock rlock;

    @Override
    public Integer shop1(String name) {
        if (rlock.lock("1")) {
            String result = redisTemplate.opsForValue().get("1").toString();
            if (Integer.valueOf(result) <= 0) {
                return -1;
            } else {
                try {
                    redisTemplate.opsForValue().set("1",Integer.valueOf(result)-1);
                    arrayBlockingQueue.put(name);
                    rlock.unlock("1");
                    return 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                while (true) {
                    Thread.sleep(2000);
                    if (rlock.lock("1")) {
                        String result = redisTemplate.opsForValue().get("1").toString();
                        if (Integer.valueOf(result) <= 0) {
                            return -1;
                        }
                        else {
                            try {
                                redisTemplate.opsForValue().set("1", Integer.valueOf(result)-1);
                                arrayBlockingQueue.put(name);
                                rlock.unlock("1");
                                return 1;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

}

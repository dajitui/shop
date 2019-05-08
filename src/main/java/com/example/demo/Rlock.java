package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * @program: demoA
 * @description
 * @author: dajitui
 * @create: 2019-01-03 23:03
 **/
@Component
public class Rlock {


    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final long expireTime = 5000;
    private static final Long RELEASE_SUCCESS = 1L;


    private static String lockKey = "message";

    @Autowired
    JedisPool jedisPool;

    /**
     * 请求id
     *
     * @param requestid
     * @return
     */
    public boolean lock(String requestid) {
        Jedis jedis = getJedis();

        String result = jedis.set(lockKey, requestid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    public synchronized Jedis getJedis() {
        Jedis jedis = jedisPool.getResource();
        if (jedisPool != null) {
            try {
                if (jedis == null) {
                    jedis = jedisPool.getResource();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jedis;
    }

    /**
     * 请求id
     *
     * @param requestid
     * @return
     */
    public boolean unlock(String requestid) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {

        }
        if (jedis != null) {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestid));

            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            return false;
        } else {
            return false;
        }

    }
}
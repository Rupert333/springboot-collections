package com.example.springbootredissingle.controller;

import com.example.springbootredissingle.entity.UserEntity;
import com.example.springbootredissingle.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springbootdemo
 * @Date: 2019/1/25 15:03
 * @Author: Mr.Zheng
 * @Description:
 */
@Slf4j
@RequestMapping("/redis")
@RestController
public class RedisController {

    private static int ExpireTime = 60;   // redis中存储的过期时间60s

    @Resource
    private RedisUtil redisUtil;

    /**
     * 普通缓存放入
     */
    @RequestMapping("set")
    public boolean redisset(String key){
        UserEntity userEntity =new UserEntity();
        userEntity.setId(Long.valueOf(1));
        userEntity.setGuid(String.valueOf(1));
        userEntity.setName("zhangsan");
        userEntity.setAge(String.valueOf(20));
        userEntity.setCreateTime(new Date());

        return redisUtil.set(key,userEntity,ExpireTime);
    }

    /**
     * 普通缓存获取
     */
    @RequestMapping("get")
    public Object redisget(String key){
        return redisUtil.get(key);
    }


    /**
     * 指定缓存失效时间
     */
    @RequestMapping("expire")
    public boolean expire(String key){
        return redisUtil.expire(key,ExpireTime);
    }

    /**
     * 获取指定key的失效时间
     * @return
     */
    @RequestMapping("getExpire")
    public long getExpire(String key){
        return redisUtil.getExpire(key);
    }

    /**
     * 判断key是否失效
     */
    @RequestMapping("hasKey")
    public boolean hasKey(String key){
        return redisUtil.hasKey(key);
    }
    
    /**
     * 删除缓存
     */
    @RequestMapping("delKeys")
    public void  delKeys(String ...key){
        redisUtil.del(key);
    }

    /**
     * 普通缓存放入并设置时间
     */
    @RequestMapping("set3")
    public boolean set(String key,String value,long time){
        return redisUtil.set(key,value,time);
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    @RequestMapping("incr")
    public long incr(String key, long delta){
        return redisUtil.incr(key,delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    @RequestMapping("decr")
    public long decr(String key, long delta){
        return redisUtil.decr(key,delta);
    }

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    @RequestMapping("hget")
    public String hget(String key,String item){
        return (String) redisUtil.hget(key,item);
    }

    /**
     * HashSet
     * @param key 键
     * @return true 成功 false 失败
     */
    @RequestMapping("hmset")
    public boolean hmset(String key) {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("key1","1");
        map.put("key2","2");
        map.put("key3","3");
        return redisUtil.hmset(key,map);
    }




}
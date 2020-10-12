/******************************************************************
 *
 *    Powered By hhdd.com.
 *
 *    Copyright (c) 2001-2019
 *    https://kada.hhdd.com/
 *
 *    Package:     com.hhdd.controller
 *
 *    Filename:    RedisTranController
 *
 *    Description: 
 *
 *    Copyright:   Copyright (c) 2001-2019
 *
 *    Company:     hhdd.com
 *
 *    @author: 王志刚
 *
 *    @version: 1.0.0
 *
 *    Create at:   2020/10/12 下午1:39
 *
 *    Revision:
 *
 *    2020/10/12 下午1:39
 *        - first revision
 *
 *****************************************************************/
package com.hhdd.controller;

import com.hhdd.common.config.RedisTemplateMap;
import com.hhdd.common.config.RedisTemplateType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * RedisTranController
 *
 * @author wangzg
 * @version 1.0.0
 * @create 2020/10/12 下午1:39
 */
@RestController
@RequestMapping("/redis")
public class RedisTranController {

    @Resource
    private RedisTemplateMap redisTemplateMap;

    @RequestMapping("/test")
    public Map<String, String> test() {
        Map<String, String> returnMap = new TreeMap<>();

        Map<Integer, Map<RedisTemplateType, RedisTemplate>> redisTMap = redisTemplateMap.getTemplateMap();
        RedisTemplate redisTemplate;
        String redisKey;
        for (int i = 0; i < 9; i++) {
            redisTemplate = redisTMap.get(i).get(RedisTemplateType.SS);
            redisKey = "multi-" + i;
            redisTemplate.opsForValue().set(redisKey, String.valueOf(i),5, TimeUnit.MINUTES);
            returnMap.put(redisKey, (String) redisTemplate.opsForValue().get(redisKey));
        }
        return returnMap;
    }

}

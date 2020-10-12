/******************************************************************
 *
 *    Powered By hhdd.com.
 *
 *    Copyright (c) 2001-2019
 *    https://kada.hhdd.com/
 *
 *    Package:     com.hhdd.common.config
 *
 *    Filename:    RedisTemplateMap
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
 *    Create at:   2020/10/12 上午11:20
 *
 *    Revision:
 *
 *    2020/10/12 上午11:20
 *        - first revision
 *
 *****************************************************************/
package com.hhdd.common.config;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * RedisTemplateMap
 *
 * @author wangzg
 * @version 1.0.0
 * @create 2020/10/12 上午11:20
 */
public class RedisTemplateMap {

    private Map<Integer, Map<RedisTemplateType,RedisTemplate>> templateMap = new HashMap();

    public  RedisTemplate get(int dbIndex, RedisTemplateType redisTemplateType) {
        return templateMap.get(dbIndex).get(redisTemplateType);
    }

    public Map<Integer, Map<RedisTemplateType, RedisTemplate>> getTemplateMap() {
        return templateMap;
    }

    public void setTemplateMap(Map<Integer, Map<RedisTemplateType, RedisTemplate>> templateMap) {
        this.templateMap = templateMap;
    }
}

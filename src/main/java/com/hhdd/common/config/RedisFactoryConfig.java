/******************************************************************
 *
 *    Powered By hhdd.com.
 *
 *    Copyright (c) 2001-2019
 *    https://kada.hhdd.com/
 *
 *    Package:     com.hhdd.common.config
 *
 *    Filename:    RedisCFactoryConfig
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
 *    Create at:   2020/10/12 上午10:23
 *
 *    Revision:
 *
 *    2020/10/12 上午10:23
 *        - first revision
 *
 *****************************************************************/
package com.hhdd.common.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RedisCFactoryConfig
 *
 * @author wangzg
 * @version 1.0.0
 * @create 2020/10/12 上午10:23
 */
@Configuration
public class RedisFactoryConfig {

    /**
     * 默认的序列化方案
     */
    private static RedisSerializer defRedisSerializer = new JdkSerializationRedisSerializer();

    private static List<RedisSerializerEntity> serializerEntities = configRedisSerializerEntities();


    @Bean("standalone")
    public RedisTemplateMap redisTemplateMap(RedisMultiDBProperties redisMultiDBProperties) {
        RedisTemplateMap redisTemplateMap = new RedisTemplateMap();
        for (int dbIndex : redisMultiDBProperties.getDatabases()) {
            createRedisTemplates(dbIndex, redisMultiDBProperties, redisTemplateMap);

        }

        return redisTemplateMap;
    }

    private RedisConnectionFactory getFactory(int dbIndex, RedisMultiDBProperties redisMultiDBProperties) {
        RedisStandaloneConfiguration standaloneConfiguration = new
                RedisStandaloneConfiguration(redisMultiDBProperties.getHost(), redisMultiDBProperties.getPort());
        standaloneConfiguration.setDatabase(dbIndex);
        standaloneConfiguration.setPassword(redisMultiDBProperties.getPassword());

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(redisMultiDBProperties.getPoolMaxIdle());
        genericObjectPoolConfig.setMinIdle(redisMultiDBProperties.getPoolMinIdle());
        genericObjectPoolConfig.setMaxTotal(redisMultiDBProperties.getPoolMaxActive());
        genericObjectPoolConfig.setMaxWaitMillis(redisMultiDBProperties.getPoolMaxWait());

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder = LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(redisMultiDBProperties.getTimeout()));

        builder.shutdownTimeout(Duration.ofMillis(redisMultiDBProperties.getShutdownTimeout()));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(standaloneConfiguration, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();

        return lettuceConnectionFactory;
    }

    /**
     * 为每一个db创建redisTemplate
     * @param dbIndex
     * @param redisMultiDBProperties
     * @param redisTemplateMap
     */
    private void createRedisTemplates(int dbIndex, RedisMultiDBProperties redisMultiDBProperties, RedisTemplateMap redisTemplateMap) {
        RedisConnectionFactory redisConnectionFactory = getFactory(dbIndex, redisMultiDBProperties);
        Map<RedisTemplateType, RedisTemplate> dbRedisMap = new HashMap<>();
        RedisTemplate redisTemplate;
        for (RedisSerializerEntity serializerEntity : serializerEntities) {
            redisTemplate = createRedisTemplate(redisConnectionFactory, serializerEntity);
            dbRedisMap.put(serializerEntity.getRedisTemplateType(), redisTemplate);
        }
        redisTemplateMap.getTemplateMap().put(dbIndex, dbRedisMap);
    }

    public RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializerEntity serializerEntity) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(serializerEntity.getKeySerializer());
        redisTemplate.setValueSerializer(serializerEntity.getValueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 配置哪几种序列化RedisTemplate
     * @return
     */
    private static List<RedisSerializerEntity> configRedisSerializerEntities() {
        List<RedisSerializerEntity> list = new ArrayList<>();
        RedisSerializerEntity SS = new RedisSerializerEntity(RedisTemplateType.SS, new StringRedisSerializer(), new StringRedisSerializer());
        list.add(SS);

        RedisSerializerEntity SO = new RedisSerializerEntity(RedisTemplateType.SO, new StringRedisSerializer(), new JdkSerializationRedisSerializer());
        list.add(SO);

        RedisSerializerEntity OO = new RedisSerializerEntity(RedisTemplateType.OO, new JdkSerializationRedisSerializer(), new JdkSerializationRedisSerializer());
        list.add(OO);

        return list;
    }

    /**
     * key和value的序列化方案组
     */
    public static class RedisSerializerEntity {

        private RedisTemplateType redisTemplateType;
        /**
         * key的序列化方案
         */
        private RedisSerializer<?> keySerializer = defRedisSerializer;

        /**
         * value的序列化方案
         */
        private RedisSerializer<?> valueSerializer = defRedisSerializer;

        public RedisSerializerEntity(RedisTemplateType redisTemplateType, RedisSerializer<?> keySerializer, RedisSerializer<?> valueSerializer) {
            this.redisTemplateType = redisTemplateType;
            this.keySerializer = keySerializer;
            this.valueSerializer = valueSerializer;
        }

        public RedisTemplateType getRedisTemplateType() {
            return redisTemplateType;
        }

        public void setRedisTemplateType(RedisTemplateType redisTemplateType) {
            this.redisTemplateType = redisTemplateType;
        }

        public RedisSerializer<?> getKeySerializer() {
            return keySerializer;
        }

        public void setKeySerializer(RedisSerializer<?> keySerializer) {
            this.keySerializer = keySerializer;
        }

        public RedisSerializer<?> getValueSerializer() {
            return valueSerializer;
        }

        public void setValueSerializer(RedisSerializer<?> valueSerializer) {
            this.valueSerializer = valueSerializer;
        }

    }
}

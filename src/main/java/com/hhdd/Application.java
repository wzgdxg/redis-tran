/******************************************************************
 *
 *    Powered By hhdd.com.
 *
 *    Copyright (c) 2001-2019
 *    https://kada.hhdd.com/
 *
 *    Package:     com.hhdd.cicada
 *
 *    Filename:    Application
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
 *    Create at:   2019-12-03 15:12
 *
 *    Revision:
 *
 *    2019-12-03 15:12
 *        - first revision
 *
 *****************************************************************/
package com.hhdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Application
 *
 * @author wangzg
 * @version 1.0.0
 * @create 2019-12-03 15:12
 */
@SpringBootApplication(scanBasePackages = "com.hhdd")
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
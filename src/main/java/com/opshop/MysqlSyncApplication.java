package com.opshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * created by wangzelong 2019/3/16 10:43
 */
@SpringBootApplication
@EnableAsync
public class MysqlSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(MysqlSyncApplication.class, args);
    }
}

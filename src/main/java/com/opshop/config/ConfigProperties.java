package com.opshop.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * created by wangzelong 2019/3/16 16:19
 */
@Data
@Component
@ConfigurationProperties(prefix = "jdbc")
@PropertySource(value = "config.properties")
public class ConfigProperties {

    private String ip;

    private Integer port;

    private String username;

    private String password;
}

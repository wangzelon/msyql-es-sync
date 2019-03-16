package com.opshop.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * created by wangzelong 2019/3/16 11:28
 */
@Configuration
public class ElasticsearchConfig {
    @Bean
    public TransportClient client() throws UnknownHostException {
        InetSocketTransportAddress node = new InetSocketTransportAddress(
                InetAddress.getByName("172.20.0.180"),
                9300
        );
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
                .build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }

}

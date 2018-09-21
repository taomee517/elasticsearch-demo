package com.es.demo.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class EsConfig {
    @Bean
    public TransportClient client() throws UnknownHostException {

        //es集群连接
        TransportAddress node = new  TransportAddress(InetAddress.getByName("localhost"),9300);
//        TransportAddress node1 = new  TransportAddress(InetAddress.getByName("localhost"),8300);
//        TransportAddress node2 = new  TransportAddress(InetAddress.getByName("localhost"),7300);

        //es集群配置（自定义配置） 连接自己安装的集群名称
        Settings settings = Settings.builder()
//                .put("cluster.name","es-cluster")
                .build();

        PreBuiltTransportClient client = new PreBuiltTransportClient(settings);

        client.addTransportAddress(node);
//        client.addTransportAddress(node1);
//        client.addTransportAddress(node2);

        return client;
    }
}

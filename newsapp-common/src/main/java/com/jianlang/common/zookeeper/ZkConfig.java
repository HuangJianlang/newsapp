package com.jianlang.common.zookeeper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//注入到spring容器中
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zk")
@PropertySource("classpath:zookeeper.properties")
public class ZkConfig {

    private String host;
    private String sequencePath;

    @Bean
    public ZookeeperClient zookeeperClient(){
        return new ZookeeperClient(this.getHost(), this.getSequencePath());
    }
}

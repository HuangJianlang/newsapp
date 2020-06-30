package com.jianlang.common.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@ConfigurationProperties(prefix="kafka.topic")
@PropertySource("classpath:kafka.properties")
public class KafkaTopicConfig {
    private String articleUpdateBus;
    private String articleIncrHandle;
}
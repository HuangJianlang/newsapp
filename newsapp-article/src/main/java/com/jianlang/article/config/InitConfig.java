package com.jianlang.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan({
        "com.jianlang.common.mysql.core",
        "com.jianlang.common.common.init",
        "com.jianlang.common.kafka",
        "com.jianlang.common.kafkastream",
        "com.jianlang.common.quartz"
})
@EnableScheduling
public class InitConfig {
}

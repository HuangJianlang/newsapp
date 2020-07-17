package com.jianlang.crawler.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
//@ComponentScan({"com.jianlang.common.common.init", "com.jianlang.common.mysql.core", "com.jianlang.common.kafka"})
// TODO: 6/14/20 kafka config
@ComponentScan({
        "com.jianlang.common.common.init",
        "com.jianlang.common.mysql.core",
        //"com.jianlang.common.quartz",
        "com.jianlang.common.elasticsearch"
})
@EnableScheduling
public class InitConfig {
}

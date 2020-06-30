package com.jianlang.behavior.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "com.jianlang.common.common.init",
        "com.jianlang.common.kafka",
})
public class InitConfig {
}

package com.jianlang.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.jianlang.common.redis")
public class RedisConfig {
}
